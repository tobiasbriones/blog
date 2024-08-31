<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# System and MSW Ops v0.1.0 | MathSwe Ops MVP (2024/08/30)

MathSwe Ops is a wide form of SWAM (Special Software and Models) that integrates
lower-level operations closer to the OS to develop and deploy MSW (Mathematical
Software). Its MVP (Minimum Viable Project) currently features undertakings to
automate OS operations for fast system cold-start.

## CLI App for OS Automation and the MSW Ops Web Home

One of the MathSwe Ops MVP challenges is automating onboarding and setup for
workstations and VMs for fast cold-start availability or restoration. It must be
capable of safely installing and restoring software without human interaction
(except when the OS asks for superuser passwords) on a case-by-case basis.

- [Designing Image Ops \| MathSwe Ops MVP (2024/08/08)](https://blog.mathsoftware.engineer/designing-image-ops---mathswe-ops-mvp-2024-08-08).
- [New Image Catalog and Landing Page \| MathSwe Ops Mvp (2024/08/22)](https://blog.mathsoftware.engineer/new-image-catalog-and-landing-page---mathswe-ops-mvp-2024-08-22).
- [New Config Command \| MathSwe Ops MVP (2024/08/30)](https://blog.mathsoftware.engineer/new-config-command---mathswe-ops-mvp-2024-08-30).

MSW Ops deployment at [Ops.Math.Software](https://ops.math.software).

GitHub release at
[System, Ops.Math.Software v0.1.0: Publishes a CLI App for OS Automation and the MSW Ops Web Home](https://github.com/mathswe-ops/mathswe-ops---mvp/releases/tag/v0.1.0).

MathSwe System Ops is a new CLI app for Ubuntu that implements the image concept
with operations such as installation, uninstallation, reinstallation, and
configuration to automate software. The MSW Ops web home presents the essential
concepts and the System app documentation.

## New System CLI App

The System CLI application is a part of the MathSwe Ops MVP to automate software
operations in Linux, such as installation, uninstallation, and configuration,
allowing you to set up server VMs and desktop Workstations by running a command.

An image is a model of a software package with OS operations to install,
uninstall, or reinstall it.

The technical documentation of this release is at
[MathSwe System Ops MVP v0.1.0 \| GitHub](https://github.com/mathswe-ops/mathswe-ops---mvp/tree/v0.1.0/system).

System is a CLI application with a reliable and evolving design that makes
**cloud VMs and desktop work machines productive from cold OS installation**. It
automates cold-start DevOps and staff onboarding as per your organization's
standards.

### Getting Started

MathSwe System Ops MVP is compatible with Ubuntu. Its
[release](https://github.com/mathswe-ops/mathswe-ops---mvp/releases/tag/v0.1.0)
attaches the `deb` installer and its `sha256` checksum.
