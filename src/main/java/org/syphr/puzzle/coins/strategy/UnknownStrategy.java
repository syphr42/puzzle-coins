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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.syphr.puzzle.coins.Coin;
import org.syphr.puzzle.coins.Scale;
import org.syphr.puzzle.coins.Strategy;

import com.google.common.collect.ImmutableList;

public class UnknownStrategy implements Strategy
{
    @Override
    public Coin findUnique(Set<Coin> coins, Scale scale)
    {
        int count = coins.size();
        if (count % 3 != 0)
        {
            throw new IllegalArgumentException(UnknownStrategy.class.getSimpleName()
                                               + " is not applicable to sets that are not divisible by 3");
        }

        List<Coin> oCoins = new ArrayList<Coin>(coins);

        int group1End = count / 3;
        int group2End = group1End * 2;

        List<Coin> group1 = oCoins.subList(0, group1End);
        List<Coin> group2 = oCoins.subList(group1End, group2End);
        List<Coin> group3 = oCoins.subList(group2End, count);

        int group12Result = scale.compare(group1, group2);
        if (group12Result == 0)
        {
            int group3Count = group3.size();
            return findUniqueGroup3(group3.subList(0, group3Count / 2),
                                    group3.subList(group3Count / 2, group3Count),
                                    scale);
        }

        List<Coin> ac = ImmutableList.<Coin>builder()
                                     .addAll(group1.subList(0, 2))
                                     .addAll(group2.subList(0, 2))
                                     .build();
        List<Coin> bd = ImmutableList.<Coin>builder()
                                     .addAll(group1.subList(2, 4))
                                     .addAll(group2.subList(2, 4))
                                     .build();

        int crossResult = scale.compare(ac, bd);

        List<Coin> x;
        List<Coin> y;
        if (Math.signum(group12Result) == Math.signum(crossResult))
        {
            x = ac.subList(0, 2); // a
            y = bd.subList(2, 4); // d
        }
        else
        {
            x = bd.subList(0, 2); // b
            y = ac.subList(2, 4); // c
        }
        return returnResult(x, y, group12Result, crossResult, scale);
    }

    private Coin findUniqueGroup3(List<Coin> group1, List<Coin> group2, Scale scale)
    {
        int group1Result = scale.compare(group1.get(0), group1.get(1));
        if (group1Result == 0)
        {
            int crossGroupResult = scale.compare(group1.get(0), group2.get(0));
            return crossGroupResult == 0 ? group2.get(1) : group2.get(0);
        }
        else
        {
            int crossGroupResult = scale.compare(group1.get(0), group2.get(0));
            return crossGroupResult == 0 ? group1.get(1) : group1.get(0);
        }
    }

    private Coin returnResult(List<Coin> group1,
                              List<Coin> group2,
                              int firstResult,
                              int secondResult,
                              Scale scale)
    {
        if (firstResult < 0)
        {
            Coin a;
            Coin b;
            if (secondResult < 0)
            {
                a = group1.get(0);
                b = group1.get(1);
            }
            else
            {
                a = group1.get(0);
                b = group1.get(1);
            }

            int thirdResult = scale.compare(a, b);
            return thirdResult < 0 ? a : b;
        }

        if (firstResult > 0)
        {
            Coin a;
            Coin b;
            if (secondResult > 0)
            {
                a = group2.get(0);
                b = group2.get(1);
            }
            else
            {
                a = group2.get(0);
                b = group2.get(1);
            }

            int thirdResult = scale.compare(a, b);
            return thirdResult < 0 ? a : b;
        }

        return null;
    }
}
