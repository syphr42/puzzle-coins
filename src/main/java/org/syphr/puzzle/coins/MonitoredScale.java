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
import java.util.Collection;
import java.util.stream.Collectors;

public class MonitoredScale implements Scale
{
    private int weighings;

    @Override
    public int compare(Collection<Coin> c1, Collection<Coin> c2)
    {
        weighings++;

        return c1.stream().collect(Collectors.summingInt(coin -> coin.getWeight()))
               - c2.stream().collect(Collectors.summingInt(coin -> coin.getWeight()));
    }

    @Override
    public int compare(Coin c1, Coin c2)
    {
        return compare(Arrays.asList(c1), Arrays.asList(c2));
    }

    public int getWeighings()
    {
        return weighings;
    }

    public void reset()
    {
        weighings = 0;
    }
}
