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

public class TrinarySearchStrategy implements Strategy
{
    @Override
    public Coin findUnique(Set<Coin> coins, Scale scale)
    {
        return findUnique(new ArrayList<Coin>(coins), scale);
    }

    private Coin findUnique(List<Coin> coins, Scale scale)
    {
        int count = coins.size();
        if (count == 1)
        {
            return coins.get(0);
        }
        if (count == 2)
        {
            return null;
        }

        int groupSize = count / 3;
        int remainder = count % 3;

        int group3Start = groupSize * 2;
        List<Coin> group1 = coins.subList(0, groupSize);
        List<Coin> group2 = coins.subList(groupSize, group3Start);
        List<Coin> group3 = coins.subList(group3Start, groupSize * 3);

        if (groupSize == 1 && remainder == 0)
        {
            return scale.compare(group1, group2) == 0
                            ? group3.get(0)
                            : scale.compare(group1, group3) == 0 ? group2.get(0) : group1.get(0);
        }
        else
        {
            boolean firstTwoGroupsMatch = scale.compare(group1, group2) == 0;

            List<Coin> newGroup = firstTwoGroupsMatch
                            ? coins.subList(group3Start, count)
                            : coins.subList(0, group3Start);

            int newCount = newGroup.size();
            if (newCount == 2)
            {
                Coin padding = firstTwoGroupsMatch ? group1.get(0) : group3.get(0);
                newGroup = ImmutableList.<Coin>builder().addAll(newGroup).add(padding).build();
            }

            return findUnique(newGroup, scale);
        }
    }
}
