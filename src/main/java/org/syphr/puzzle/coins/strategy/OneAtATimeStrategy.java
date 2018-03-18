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
package org.syphr.puzzle.coins.strategy;

import java.util.Iterator;
import java.util.Set;

import org.syphr.puzzle.coins.Coin;
import org.syphr.puzzle.coins.Scale;
import org.syphr.puzzle.coins.Strategy;

public class OneAtATimeStrategy implements Strategy
{
    @Override
    public Coin findUnique(Set<Coin> coins, Scale scale)
    {
        Iterator<Coin> iter = coins.iterator();

        Coin firstCoin = iter.next();
        Coin secondCoin = iter.next();

        int firstPairResult = scale.compare(firstCoin, secondCoin);

        while (iter.hasNext())
        {
            Coin coin = iter.next();

            if (firstPairResult == 0)
            {
                int result = scale.compare(coin, firstCoin);
                if (result != 0)
                {
                    return coin;
                }
            }
            else
            {
                int firstResult = scale.compare(coin, firstCoin);
                return firstResult == 0 ? secondCoin : firstCoin;
            }
        }

        return null;
    }
}
