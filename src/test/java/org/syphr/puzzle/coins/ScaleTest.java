/**
 * Copyright 2018 Gregory Moyer and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.syphr.puzzle.coins;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScaleTest
{
    Scale scale;

    @BeforeEach
    void setup()
    {
        scale = new Scale();
    }

    @Test
    void returnsPositiveNumberComparedToSmaller()
    {
        assertAll(() -> assertTrue(scale.compare(coins(2), coins(1)) > 0),
                  () -> assertTrue(scale.compare(coins(1, 1), coins(1)) > 0),
                  () -> assertTrue(scale.compare(coins(3), coins(1, 1)) > 0),
                  () -> assertTrue(scale.compare(coins(1, 2, 3), coins(2, 2, 1)) > 0),
                  () -> assertTrue(scale.compare(coins(1, 2, 3), coins(1, 2)) > 0),
                  () -> assertTrue(scale.compare(coins(4, 5), coins(1, 2, 3)) > 0));
    }

    @Test
    void returnsNegativeNumberComparedToLarger()
    {
        assertAll(() -> assertTrue(scale.compare(coins(1), coins(2)) < 0),
                  () -> assertTrue(scale.compare(coins(1), coins(1, 1)) < 0),
                  () -> assertTrue(scale.compare(coins(1, 1), coins(3)) < 0),
                  () -> assertTrue(scale.compare(coins(2, 2, 1), coins(1, 2, 3)) < 0),
                  () -> assertTrue(scale.compare(coins(1, 2), coins(1, 2, 3)) < 0),
                  () -> assertTrue(scale.compare(coins(1, 2, 3), coins(4, 5)) < 0));
    }

    @Test
    void returnsZeroComparedToEqual()
    {
        assertAll(() -> assertTrue(scale.compare(coins(1), coins(1)) == 0),
                  () -> assertTrue(scale.compare(coins(2), coins(1, 1)) == 0),
                  () -> assertTrue(scale.compare(coins(1, 2), coins(3)) == 0),
                  () -> assertTrue(scale.compare(coins(3, 2, 1), coins(1, 2, 3)) == 0),
                  () -> assertTrue(scale.compare(coins(4, 2), coins(1, 2, 3)) == 0),
                  () -> assertTrue(scale.compare(coins(3, 3, 3), coins(4, 5)) == 0));
    }

    @Test
    void countWeighings()
    {
        List<Coin> coins = coins(1);

        scale.compare(coins, coins);
        assertEquals(1, scale.getWeighings());

        scale.compare(coins, coins);
        assertEquals(2, scale.getWeighings());

        scale.compare(coins, coins);
        assertEquals(3, scale.getWeighings());
    }

    Coin coin(int weight)
    {
        return new Coin(weight);
    }

    List<Coin> coins(int... weights)
    {
        return Arrays.stream(weights).mapToObj(weight -> coin(weight)).collect(Collectors.toList());
    }
}
