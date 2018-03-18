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
import static org.syphr.puzzle.coins.DataGenerator.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.syphr.puzzle.coins.RunResult.Outcome;

class RunnerTest
{
    @Test
    void success()
    {
        Runner runner = new Runner(new Strategy()
        {
            @Override
            public Coin findUnique(Set<Coin> coins, Scale scale)
            {
                scale.compare(coin(1), coin(1));
                return coins.iterator().next();
            }
        });

        List<Coin> coins = coins(1);
        Scenario scenario = scenario(coins, coins.get(0));

        List<RunResult> results = runner.run(new MonitoredScale(), Arrays.asList(scenario));
        assertEquals(1, results.size());

        RunResult result = results.get(0);
        assertEquals(Outcome.SUCCESS, result.getOutcome());
        assertEquals(1, result.getWeighings());
    }

    @Test
    void noCoinFound()
    {
        Runner runner = new Runner(new Strategy()
        {
            @Override
            public Coin findUnique(Set<Coin> coins, Scale scale)
            {
                scale.compare(coin(1), coin(1));
                scale.compare(coin(1), coin(1));
                return null;
            }
        });

        List<Coin> coins = coins(1);
        Scenario scenario = scenario(coins, coins.get(0));

        List<RunResult> results = runner.run(new MonitoredScale(), Arrays.asList(scenario));
        assertEquals(1, results.size());

        RunResult result = results.get(0);
        assertEquals(Outcome.NO_COIN, result.getOutcome());
        assertEquals(2, result.getWeighings());
    }

    @Test
    void wrongCoinFound()
    {
        Runner runner = new Runner(new Strategy()
        {
            @Override
            public Coin findUnique(Set<Coin> coins, Scale scale)
            {
                scale.compare(coin(1), coin(1));
                scale.compare(coin(1), coin(1));
                scale.compare(coin(1), coin(1));
                return coins.iterator().next();
            }
        });

        List<Coin> coins = coins(1, 2);
        Scenario scenario = scenario(coins, coins.get(1));

        List<RunResult> results = runner.run(new MonitoredScale(), Arrays.asList(scenario));
        assertEquals(1, results.size());

        RunResult result = results.get(0);
        assertEquals(Outcome.WRONG_COIN, result.getOutcome());
        assertEquals(3, result.getWeighings());
    }
}
