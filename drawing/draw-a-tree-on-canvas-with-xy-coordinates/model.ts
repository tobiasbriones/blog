/*
 * Copyright (c) 2022 Tobias Briones. All rights reserved.
 *
 * SPDX-License-Identifier: MIT
 *
 * This file is part of Math Software Engineer's Blog.
 *
 * This file is also available at https://github.com/repsymo/2dp-repsymo-solver
 * under a different license.
 *
 * This source code is licensed under the MIT License found in the LICENSE-MIT
 * file in the root directory of this source tree or at
 * https://opensource.org/licenses/MIT.
 */

export interface TreeNode {
  machineAge: number;
  decisionYear: number;
  k?: TreeNode;
  r?: TreeNode;
}

export function newTreeNode(): TreeNode {
  return {
    machineAge: 0,
    decisionYear: 0,
    k: null,
    r: null
  };
}
