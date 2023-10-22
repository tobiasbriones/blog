document.addEventListener('DOMContentLoaded', initApp);

function initApp() {
  const nav = document.querySelector('header > nav');
  const header = document.querySelector('header');
  const toc = document.querySelector('nav.toc');

  updateFilePath();
  initHashNav();
  initMenu();
  initNav();
  initFullScreenElements();

  function initHashNav() {
    document
      .querySelectorAll('a')
      .forEach(anchor => {
        const href = anchor.getAttribute('href');

        if (href.startsWith("#")) {
          anchor.addEventListener('click', e => {
            e.preventDefault();

            if (href === '#') {
              history.replaceState({}, document.title, window.location.pathname + window.location.search);
            } else {
              window.location.hash = href;
            }
          })
        }
      });
  }

  function initMenu() {
    const navButton = document.getElementById('nav-item');

    navButton.addEventListener('click', onNavItemClick);

    function onNavItemClick() {
      if (isNavShowing()) {
        hideNav();
      } else {
        showNav();
      }
    }
  }

  function initNav() {
    const anchors = document.querySelectorAll('nav.toc a');
    let mobile = isMobile();

    const updateSelectedAnchor = hash => {
      anchors.forEach(anchor => {
        if (anchor.getAttribute('href') === hash) {
          anchor.classList.add('selected');
          makeAnchorVisibleInNav(anchor);
        } else {
          anchor.classList.remove('selected');
        }
      });
    };


    anchors.forEach(anchor => {
      anchor.addEventListener('click', event => {
        event.preventDefault();
        if (mobile) {
          hideNav();
        }
        scrollTo(anchor);
      });
    });
    updateSelectedAnchor(window.location.hash);

    if (!mobile) {
      showNav();
    }

    window.addEventListener('hashchange', () => updateSelectedAnchor(window.location.hash));
    window.addEventListener('scroll', selectAnchorFromScrollPosition);
    window.addEventListener('load', scrollToHashHeading);
    window.addEventListener('resize', () => {
      const newMobile = isMobile();

      if (newMobile !== mobile) {
        // Changed from desktop to mobile
        if (newMobile) {
          hideNav();
        } else {
          showNav();
        }
        mobile = newMobile;
      }
    });


    function makeAnchorVisibleInNav(anchor) {
      const anchorRect = anchor.getBoundingClientRect();
      const navRect = nav.getBoundingClientRect();

      if (anchorRect.top < navRect.top || anchorRect.bottom > navRect.bottom) {
        nav.scrollTo({
          top: anchorRect.top + navRect.height / 2,
          behavior: 'smooth',
        });
      }
    }

    function selectAnchorFromScrollPosition() {
      const focusedHeading = getFocusedHeading();
      const headingId = focusedHeading.getAttribute('id');
      const hash = `#${headingId}`;

      if (headingId) {
        updateSelectedAnchor(hash);
      }
    }

    function scrollToHashHeading() {
      if (!window.location.hash.startsWith('#')) {
        return;
      }
      const hash = window.location.hash;
      const heading = document.querySelector(hash);
      heading.scrollIntoView({
        behavior: 'smooth',
        block: 'start',
      });
    }
  }

  function isNavShowing() {
    return header.classList.contains('show');
  }

  function showNav() {
    header.classList.add('show');

    if (toc) {
      toc.classList.add('show');
    }
  }

  function hideNav() {
    header.classList.remove('show');

    if (toc) {
      toc.classList.remove('show');
    }
  }

  function isMobile() {
    return window.innerWidth < 1280;
  }
}

function scrollTo(anchor) {
  if (!anchor.href) {
    return;
  }
  const href = anchor.attributes.href.value;
  const targetId = href.substring(href.indexOf('#'));

  if (targetId === '#') {
    document.body.scrollIntoView({
      behavior: 'smooth',
      block: 'start',
    });
    return;
  }

  const targetSection = document.querySelector(targetId);

  if (targetSection) {
    targetSection.scrollIntoView({
      behavior: 'smooth',
      block: 'start',
    });
  }
}

function getFocusedHeading() {
  const headings = selectHeadings();
  const isVisible = heading => {
    const rect = heading.getBoundingClientRect();
    return rect.bottom >= 0 && rect.bottom < window.innerHeight
  };
  const visibleHeadings = [];
  let invisibleHeadingClosestToTop = headings[0];

  for (let i = 0; i < headings.length; i++) {
    const heading = headings[i];

    if (isVisible(heading)) {
      visibleHeadings.push(heading);
    }
    // Check for the heading right above the page
    else {
      const rect = heading.getBoundingClientRect();
      const maxRect = invisibleHeadingClosestToTop.getBoundingClientRect();

      if (rect.bottom < 0) {
        if (rect.bottom > maxRect.bottom) {
          invisibleHeadingClosestToTop = heading;
        }
      }
    }
  }

  // Return the closest heading if none is visible
  if (visibleHeadings.length === 0) {
    return invisibleHeadingClosestToTop;
  }

  // Return the heading in the top 1/3 of the page (normal behavior)
  for (const heading of visibleHeadings) {
    const rect = heading.getBoundingClientRect();

    if (rect.top < window.innerHeight / 3) {
      return heading;
    }
  }
  return invisibleHeadingClosestToTop;
}

function initFullScreenElements() {
  document
    .querySelectorAll('.fullscreen')
    .forEach(fullscreenButton => {
      const parent = fullscreenButton.parentElement;

      fullscreenButton.addEventListener('click', () => {
        if (document.fullscreenElement) {
          document.exitFullscreen();
        } else {
          goFullscreen(parent);
        }
      });
    });

  document
    .querySelectorAll('.zoom')
    .forEach(zoomButton => {
      const parent = zoomButton.parentElement;

      zoomButton.addEventListener('click', () => {
        // Apply zoom to all slide images
        if (parent.classList.contains('zoom-in')) {
          parent.classList.remove('zoom-in');
        } else {
          parent.classList.add('zoom-in');
        }
      });
    });

  function goFullscreen(parent) {
    parent.requestFullscreen();
    parent.classList.add('fullscreen-active');
    parent.addEventListener('fullscreenchange', onFullscreenChange);

    window.addEventListener('resize', onResize);

    function onResize() {
      if (document.fullscreenElement) {
        setFullscreenDimensions(parent);
      }
    }

    function onFullscreenChange() {
      // Exiting fullscreen
      if (!document.fullscreenElement) {
        removeFullscreenDimensions(parent);
        window.removeEventListener('resize', onResize);
        parent.removeEventListener('fullscreenchange', onFullscreenChange);
      }
    }
  }

  function setFullscreenDimensions(parent) {
    const viewportWidth = window.innerWidth;
    const viewportHeight = window.innerHeight;

    // Apply the scaling transform to all slides
    parent
      .querySelectorAll('img')
      .forEach(img => {
        const imgWidth = img.width;
        const imgHeight = img.height;
        const widthRatio = viewportWidth / imgWidth;
        // Calculate image size with width=100% to the img as the baseline
        const fitImgHeight = imgHeight * widthRatio;
        const viewportAspectRatio = viewportWidth / viewportHeight;
        const imgAspectRatio = imgWidth / imgHeight;

        // VP is wider than Image (portrait), fit height
        if (viewportAspectRatio > imgAspectRatio) {
          const scale = viewportHeight / fitImgHeight;
          img.style.transform = `scale(${scale})`;
        }

        // Else Image (landscape) is wider than VP, fit the width
        // so width=100% by CSS, and scale=1 by default
      });
  }

  function removeFullscreenDimensions(parent) {
    parent.classList.remove('fullscreen-active');
    parent
      .querySelectorAll('img')
      .forEach(img => {
        img.style.transform = 'none';
      });
  }
}

function onCopyCodeSnippet(button) {
  const code = button.getAttribute("data-code");
  const tooltip = button.querySelector(".tooltip");

  navigator
    .clipboard
    .writeText(code)
    .then(() => {
      tooltip.classList.add("show");

      setTimeout(() => tooltip.classList.remove("show"), 2000)
    })
    .catch((reason) => console.log(`Failed to copy code to clipboard: ${reason}`))
}

function onOpenCodeSnippetLink(button) {
  const path = button.getAttribute("data-path");
  const currentURL = window.location.href.split('#')[0]; // Remove the hash part if it exists
  const newURL = currentURL + '/' + path;

  window.open(newURL, '_blank');
}

function updateFilePath() {
  const pageTitle = document.querySelector('h1').textContent;

  // It's a file like DataTest.java
  // This path update is necessary since Netlify lowercase the path
  if (pageTitle.includes('.')) {
    const currentPath = window.location.pathname;
    const pathSegments = currentPath.split('/');

    pathSegments[pathSegments.length - 1] = pageTitle;
    const newPath = pathSegments.join('/');

    history.replaceState(null, null, newPath);
  }
}

function selectHeadings() {
  return document.querySelectorAll('h1, h2, h3, h4, h5, h6');
}
