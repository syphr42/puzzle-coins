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

public class ThreePhaseStrategy implements Strategy
{
    @Override
    public Coin findUnique(Set<Coin> coins, Scale scale)
    {
        int count = coins.size();
        if (count % 3 != 0)
        {
            throw new IllegalArgumentException(ThreePhaseStrategy.class.getSimpleName()
                                               + " is not applicable to sets that are not divisible by 3");
        }

        List<Coin> oCoins = new ArrayList<Coin>(coins);

        int group1End = count / 3;
        int group2End = group1End * 2;

        List<Coin> group1 = oCoins.subList(0, group1End);
        List<Coin> group2 = oCoins.subList(group1End, group2End);
        List<Coin> group3 = oCoins.subList(group2End, count);

        int group3Count = group3.size();
        int compareResult = scale.compare(group1, group2);
        return compareResult == 0
                        ? findUniquePhase3Groups(group3.subList(0, group3Count / 2),
                                                 group3.subList(group3Count / 2, group3Count),
                                                 scale,
                                                 compareResult)
                        : findUniquePhase2(group1, group2, scale, compareResult);
    }

    private Coin findUniquePhase2(List<Coin> group1,
                                  List<Coin> group2,
                                  Scale scale,
                                  int initialResult)
    {
        int count = group1.size();
        if (count % 2 != 0)
        {
            throw new IllegalArgumentException(ThreePhaseStrategy.class.getSimpleName()
                                               + " is not applicable to sets where [count / 3] is not a multiple of 2");
        }

        List<Coin> group1A = group1.subList(0, count / 2);
        List<Coin> group1B = group1.subList(count / 2, count);

        int compareResult = scale.compare(group1A, group1B);
        if (compareResult == 0)
        {
            return findUniquePhase3Groups(group2.subList(0, count / 2),
                                          group2.subList(count / 2, count),
                                          scale,
                                          initialResult * -1);
        }

        if (group1A.size() > 2)
        {
            return findUniquePhase2(group1A, group1B, scale, initialResult);
        }

        if (initialResult == 0)
        {
            return findUniquePhase3Groups(group1A, group1B, scale, initialResult);
        }

        if (Math.signum(initialResult) == Math.signum(compareResult))
        {
            return findUniquePhase3Singles(group1A.get(0), group1A.get(1), scale, initialResult);
        }
        else
        {
            return findUniquePhase3Singles(group1B.get(0), group1B.get(1), scale, initialResult);
        }
    }

    private Coin findUniquePhase3Groups(List<Coin> group1,
                                        List<Coin> group2,
                                        Scale scale,
                                        int initialResult)
    {
        if (group1.size() > 2)
        {
            return findUniquePhase2(group1, group2, scale, initialResult);
        }

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

    private Coin findUniquePhase3Singles(Coin c1, Coin c2, Scale scale, int initialResult)
    {
        int finalResult = scale.compare(c1, c2);
        return Math.signum(initialResult) == Math.signum(finalResult) ? c1 : c2;
    }
}
