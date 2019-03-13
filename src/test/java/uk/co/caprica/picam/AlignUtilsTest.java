/*
 * This file is part of picam.
 *
 * picam is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * picam is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with picam.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2016-2019 Caprica Software Limited.
 */

package uk.co.caprica.picam;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static uk.co.caprica.picam.AlignUtils.alignUp;

public class AlignUtilsTest {

    @Test
    public void testAlignUpZero() {
        assertEquals(0, alignUp(0, 32));
    }

    @Test
    public void testAlignUpAtBoundary() {
        assertEquals(32, alignUp(32, 32));
        assertEquals(64, alignUp(64, 32));
        assertEquals(96, alignUp(96, 32));
    }

    @Test
    public void testAlignUp() {
        assertEquals(32, alignUp(1, 32));
        assertEquals(32, alignUp(31, 32));
        assertEquals(64, alignUp(33, 32));
        assertEquals(64, alignUp(63, 32));
        assertEquals(96, alignUp(65, 32));
        assertEquals(96, alignUp(95, 32));
        assertEquals(128, alignUp(97, 32));
    }

    @Test
    public void testLimits() {
        assertEquals(2592, alignUp(2592, 32));
        assertEquals(1952, alignUp(1944, 16));
    }
}
