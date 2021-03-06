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
import java.util.Set;

import com.google.common.collect.Sets;

public class Scenario
{
    private final String description;
    private final int uniqueCoinId;
    private final Set<Coin> coins;

    public Scenario(String description, int uniqueCoinId, Coin... coins)
    {
        this.description = description;
        this.uniqueCoinId = uniqueCoinId;
        this.coins = Sets.newLinkedHashSet(Arrays.asList(coins));
    }

    public String getDescription()
    {
        return description;
    }

    public int getUniqueCoinId()
    {
        return uniqueCoinId;
    }

    public Set<Coin> getCoins()
    {
        return coins;
    }
}
