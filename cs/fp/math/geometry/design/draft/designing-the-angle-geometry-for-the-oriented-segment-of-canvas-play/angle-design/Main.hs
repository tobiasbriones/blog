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
  AxisAngle :: QuadrantalAngle -> MeasuredAngle
  InQuadrantAngle :: QuadrantAngle q -> MeasuredAngle
