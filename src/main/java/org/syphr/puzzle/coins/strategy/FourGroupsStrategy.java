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

public class FourGroupsStrategy implements Strategy
{
    @Override
    public Coin findUnique(Set<Coin> coins, Scale scale)
    {
        int count = coins.size();
        if (count % 4 != 0)
        {
            throw new IllegalArgumentException(FourGroupsStrategy.class.getSimpleName()
                                               + " is not applicable to sets that are not divisible by 4");
        }

        List<Coin> oCoins = new ArrayList<Coin>(coins);

        int group1End = count / 4;
        int group2End = group1End * 2;
        int group3End = group1End * 3;

        List<Coin> group1 = oCoins.subList(0, group1End);
        List<Coin> group2 = oCoins.subList(group1End, group2End);
        List<Coin> group3 = oCoins.subList(group2End, group3End);
        List<Coin> group4 = oCoins.subList(group3End, count);

        int group12Result = (int)Math.signum(scale.compare(group1, group2));
        int group13Result = (int)Math.signum(scale.compare(group1, group3));

        switch (group12Result)
        {
            case 0:
            {
                switch (group13Result)
                {
                    case 0:
                    {
                        int c12Result = (int)Math.signum(scale.compare(group4.get(0),
                                                                       group4.get(1)));
                        switch (c12Result)
                        {
                            case 0:
                                return group4.get(2);
                            default:
                            {
                                int c13Result = (int)Math.signum(scale.compare(group4.get(0),
                                                                               group4.get(2)));
                                return c13Result != 0 ? group4.get(0) : group4.get(1);
                            }

                        }
                    }

                    default:
                        return findUniqueInThree(group3, -group13Result, scale);
                }
            }

            default:
            {
                switch (group13Result)
                {
                    case 0:
                        return findUniqueInThree(group2, -group12Result, scale);

                    default:
                        return findUniqueInThree(group1, group12Result, scale);
                }
            }
        }
    }

    private Coin findUniqueInThree(List<Coin> group, int priorResult, Scale scale)
    {
        int c12Result = (int)Math.signum(scale.compare(group.get(0), group.get(1)));

        if (c12Result == 0)
        {
            return group.get(2);
        }

        return priorResult == c12Result ? group.get(0) : group.get(1);
    }
}
