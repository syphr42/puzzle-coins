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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataGenerator
{
    private static int idGenerator = 0;

    public static Coin coin(int weight)
    {
        return new Coin(idGenerator++, weight);
    }

    public static List<Coin> coins(int... weights)
    {
        return Arrays.stream(weights).mapToObj(weight -> coin(weight)).collect(Collectors.toList());
    }

    public static Scenario scenario(List<Coin> coins, Coin uniqueCoin)
    {
        return new Scenario("test", uniqueCoin.getId(), coins.toArray(new Coin[coins.size()]));
    }
}
