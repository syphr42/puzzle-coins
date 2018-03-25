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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.syphr.puzzle.coins.RunResult.Outcome;

import com.google.common.base.Strings;

public class Runner
{
    private final Strategy strategy;

    public Runner(Strategy strategy)
    {
        this.strategy = strategy;
    }

    public List<RunResult> run(MonitoredScale scale, List<Scenario> scenarios)
    {
        List<RunResult> results = new ArrayList<>();

        for (Scenario scenario : scenarios)
        {
            Coin uniqueCoin = strategy.findUnique(scenario.getCoins(), scale);

            Outcome outcome;
            if (uniqueCoin == null)
            {
                outcome = Outcome.NO_COIN;
            }
            else if (uniqueCoin.getId() != scenario.getUniqueCoinId())
            {
                outcome = Outcome.WRONG_COIN;
            }
            else
            {
                outcome = Outcome.SUCCESS;
            }

            results.add(new RunResult(scenario, outcome, scale.getWeighings()));
            scale.reset();
        }

        return results;
    }

    public static void main(String[] args) throws InstantiationException,
                                           IllegalAccessException,
                                           ClassNotFoundException,
                                           IOException
    {
        if (args.length < 1)
        {
            System.out.println("Strategy class name is required.");
            System.exit(1);
        }

        Strategy strategy = (Strategy)Class.forName(args[0]).newInstance();
        List<RunResult> results = new Runner(strategy).run(new MonitoredScale(),
                                                           DataLoader.defaultScenarios());

        printResults(results);
    }

    private static void printResults(List<RunResult> results)
    {
        String divider = "--------------------------------------------|------------|------------";

        System.out.println(divider);
        System.out.println("Scenario                                    | Outcome    | Weighings |");
        System.out.println(divider);

        for (RunResult result : results)
        {
            StringBuilder b = new StringBuilder();
            b.append(Strings.padEnd(result.getScenario().getDescription(), 43, ' '))
             .append(" | ")
             .append(Strings.padEnd(result.getOutcome().toString(), 10, ' '))
             .append(" | ")
             .append(Strings.padEnd(String.valueOf(result.getWeighings()), 9, ' '))
             .append(" |");

            System.out.println(b);
        }

        System.out.println(divider);
    }
}
