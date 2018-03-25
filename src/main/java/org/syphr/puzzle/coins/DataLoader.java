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
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DataLoader
{
    public static List<Scenario> twelveCoinScenarios() throws IOException
    {
        try (Reader r = new InputStreamReader(Runner.class.getResourceAsStream("/org/syphr/puzzle/coins/12coins.json")))
        {
            Type type = new TypeToken<List<Scenario>>()
            {}.getType();
            return new Gson().fromJson(r, type);
        }
    }

    public static List<Scenario> allScenarios() throws IOException
    {
        return twelveCoinScenarios();
    }
}
