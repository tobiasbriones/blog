// Copyright (c) 2024 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/blog

import kotlin.test.Test
import kotlin.test.assertEquals

class EmailTest {
    @Test
    fun `test uniqueEmailsNum with unique locals`() {
        val emails = arrayOf(
            "test.email@gmail.com",
            "test.email@yahoo.com",
            "test.email@outlook.com"
        )
        assertEquals(3, uniqueEmailsNum(emails))
    }

    @Test
    fun `test uniqueEmailsNum with duplicate emails`() {
        val emails = arrayOf(
            "test.email@gmail.com",
            "test.email@gmail.com",
            "test.email@outlook.com"
        )
        assertEquals(2, uniqueEmailsNum(emails))
    }

    @Test
    fun `test uniqueEmailsNum with invalid emails`() {
        val emails = arrayOf(
            "test.email@gmail.com",
            "notAnEmail",
            "testemail@outlook.com"
        )
        assertEquals(2, uniqueEmailsNum(emails))
    }

    @Test
    fun `test uniqueEmailsNum with empty array`() {
        val emails = emptyArray<String>()
        assertEquals(0, uniqueEmailsNum(emails))
    }

    @Test
    fun `test uniqueEmailsNum with emails containing plus symbol`() {
        val emails = arrayOf(
            "test.email+spam@gmail.com",
            "test.email+news@yahoo.com",
            "test.email+update@outlook.com"
        )
        assertEquals(3, uniqueEmailsNum(emails))
    }

    @Test
    fun `test uniqueEmailsNum with emails containing plus symbol and duplicate local parts`() {
        val emails = arrayOf(
            "test.email+spam@gmail.com",
            "test.email+spam@gmail.com",
            "test.email+update@outlook.com"
        )
        assertEquals(2, uniqueEmailsNum(emails))
    }

    @Test
    fun `test uniqueEmailsNum with emails containing plus symbol and dots`() {
        val emails = arrayOf(
            "test.email+spam@gmail.com",
            "test.email+spam.news@yahoo.com",
            "testemail+spam.news1@yahoo.com",
            "test.email+update@outlook.com"
        )
        assertEquals(3, uniqueEmailsNum(emails))
    }
}
