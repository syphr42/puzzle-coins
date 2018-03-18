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

public class Coin
{
    private final int id;
    private final int weight;

    public Coin(int id, int weight)
    {
        this.id = id;
        this.weight = weight;
    }

    public int getId()
    {
        return id;
    }

    public int getWeight()
    {
        return weight;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof Coin))
        {
            return false;
        }
        Coin other = (Coin)obj;
        if (id != other.id)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Coin [id=");
        builder.append(id);
        builder.append(", weight=");
        builder.append(weight);
        builder.append("]");
        return builder.toString();
    }
}
