---
permalink: designing-the-angle-geometry-for-an-oriented-segment/angle-design/Main.hs.html
title: "designing-the-angle-geometry-for-an-oriented-segment/angle-design/Main.hs"
---

# Main.hs
```haskell
-- Copyright (c) 2024 Tobias Briones. All rights reserved.
-- SPDX-License-Identifier: BSD-3-Clause
-- This file is part of https://github.com/tobiasbriones/blog

{-# LANGUAGE DataKinds #-}
{-# LANGUAGE GADTs #-}
{-# LANGUAGE TypeFamilies #-}

import Data.Maybe

-- mod Angle
newtype Angle = Angle Double
  deriving (Show, Num)

newtype Acute = Acute Angle -- (0-90)

newtype Obtuse = Obtuse Angle -- (90-180)

newtype ReflexObtuse = ReflexObtuse Angle -- (180-270)

newtype ReflexAcute = ReflexAcute Angle -- (180-360)

data QuadrantalAngle
  = Zero
  | Right
  | Straight
  | ReflexRight

angle :: QuadrantalAngle -> Angle
angle x = Angle $ case x of
  Zero -> 0
  Main.Right -> 90
  Straight -> 180
  ReflexRight -> 270

data Quadrant
  = QI
  | QII
  | QIII
  | QIV

data QuadrantAngle (q :: Quadrant) where
  AngleI :: Acute -> QuadrantAngle 'QI
  AngleII :: Obtuse -> QuadrantAngle 'QII
  AngleIII :: ReflexObtuse -> QuadrantAngle 'QIII
  AngleIV :: ReflexAcute -> QuadrantAngle 'QIV

type family AngleQuadrant a :: Quadrant where
  AngleQuadrant Acute = 'QI
  AngleQuadrant Obtuse = 'QII
  AngleQuadrant ReflexObtuse = 'QIII
  AngleQuadrant ReflexAcute = 'QIV

class ToQuadrantAngle a where
  toQuadrantAngle :: a -> QuadrantAngle (AngleQuadrant a)

instance ToQuadrantAngle Acute where
  toQuadrantAngle = AngleI

instance ToQuadrantAngle Obtuse where
  toQuadrantAngle = AngleII

instance ToQuadrantAngle ReflexObtuse where
  toQuadrantAngle = AngleIII

instance ToQuadrantAngle ReflexAcute where
  toQuadrantAngle = AngleIV


data MeasuredAngle where -- [0-360)
  InAxisAngle :: QuadrantalAngle -> MeasuredAngle
  InQuadrantAngle :: QuadrantAngle q -> MeasuredAngle


-- Drafts for General Conclusions --

class Orientation a orientation where
  orientation :: a -> orientation

data AxisOrientation = Horizontal | Vertical

instance Orientation QuadrantalAngle AxisOrientation where
  orientation x = case x of
    Zero -> Horizontal
    Straight -> orientation Zero
    Main.Right -> Vertical
    ReflexRight -> orientation Main.Right

data AcuteOrientation
  = Acute15
  | Acute30
  | Acute45
  | Acute60

instance Orientation Acute (Maybe AcuteOrientation) where
  orientation (Acute (Angle a))
    | a == 15 = Just Acute15
    | a == 30 = Just Acute30
    | a == 45 = Just Acute45
    | a == 60 = Just Acute60
    | otherwise = Nothing


-- mod Algebra
data Sign = Positive | Negative


-- Imported from mod Shape
class Area a where
  area :: a -> Double


class Minus a where
  minus :: a -> a


-- mod Shape.Line
data Line = Segment Double Double Double Double

instance Area Line where
  area _ = 0

instance Minus Line where
  --  Dummy implementation, the structure is what mattered here
  minus (Segment sx sy ex ey) = Segment (sx - 1) (sy - 1) (ex - 1) (ey - 1)


main = do
  let angle1 = InQuadrantAngle $ AngleI $ Acute 8
  let angle2 = InAxisAngle $ Straight
  let a3 = toQuadrantAngle $ Acute 48
  print $ Angle 2

```
<div class="social open-gh-btn my-4">
  <a class="btn btn-github" href="https://github.com/tobiasbriones/blog/tree/main/cs/fp/math/geometry/design/draft/designing-the-angle-geometry-for-an-oriented-segment/angle-design/Main.hs" target="_blank">
    <i class="fab fa-github">
      
    </i>
    <span>
      Open in GitHub
    </span>
  </a>
</div>