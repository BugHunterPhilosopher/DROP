
package org.drip.spaces.big;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2020 Lakshmi Krishnamurthy
 * 
 *  This file is part of DROP, an open-source library targeting analytics/risk, transaction cost analytics,
 *  	asset liability management analytics, capital, exposure, and margin analytics, valuation adjustment
 *  	analytics, and portfolio construction analytics within and across fixed income, credit, commodity,
 *  	equity, FX, and structured products. It also includes auxiliary libraries for algorithm support,
 *  	numerical analysis, numerical optimization, spline builder, model validation, statistical learning,
 *  	and computational support.
 *  
 *  	https://lakshmidrip.github.io/DROP/
 *  
 *  DROP is composed of three modules:
 *  
 *  - DROP Product Core - https://lakshmidrip.github.io/DROP-Product-Core/
 *  - DROP Portfolio Core - https://lakshmidrip.github.io/DROP-Portfolio-Core/
 *  - DROP Computational Core - https://lakshmidrip.github.io/DROP-Computational-Core/
 * 
 * 	DROP Product Core implements libraries for the following:
 * 	- Fixed Income Analytics
 * 	- Loan Analytics
 * 	- Transaction Cost Analytics
 * 
 * 	DROP Portfolio Core implements libraries for the following:
 * 	- Asset Allocation Analytics
 *  - Asset Liability Management Analytics
 * 	- Capital Estimation Analytics
 * 	- Exposure Analytics
 * 	- Margin Analytics
 * 	- XVA Analytics
 * 
 * 	DROP Computational Core implements libraries for the following:
 * 	- Algorithm Support
 * 	- Computation Support
 * 	- Function Analysis
 *  - Model Validation
 * 	- Numerical Analysis
 * 	- Numerical Optimizer
 * 	- Spline Builder
 *  - Statistical Learning
 * 
 * 	Documentation for DROP is Spread Over:
 * 
 * 	- Main                     => https://lakshmidrip.github.io/DROP/
 * 	- Wiki                     => https://github.com/lakshmiDRIP/DROP/wiki
 * 	- GitHub                   => https://github.com/lakshmiDRIP/DROP
 * 	- Repo Layout Taxonomy     => https://github.com/lakshmiDRIP/DROP/blob/master/Taxonomy.md
 * 	- Javadoc                  => https://lakshmidrip.github.io/DROP/Javadoc/index.html
 * 	- Technical Specifications => https://github.com/lakshmiDRIP/DROP/tree/master/Docs/Internal
 * 	- Release Versions         => https://lakshmidrip.github.io/DROP/version.html
 * 	- Community Credits        => https://lakshmidrip.github.io/DROP/credits.html
 * 	- Issues Catalog           => https://github.com/lakshmiDRIP/DROP/issues
 * 	- JUnit                    => https://lakshmidrip.github.io/DROP/junit/index.html
 * 	- Jacoco                   => https://lakshmidrip.github.io/DROP/jacoco/index.html
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   	you may not use this file except in compliance with the License.
 *   
 *  You may obtain a copy of the License at
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  	distributed under the License is distributed on an "AS IS" BASIS,
 *  	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  
 *  See the License for the specific language governing permissions and
 *  	limitations under the License.
 */

/**
 * <i>MoviesInFlight</i> implements a Closest Pair of Movies matching a Flight Duration.
 * 
 * <br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/ComputationalCore.md">Computational Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/StatisticalLearningLibrary.md">Statistical Learning Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/spaces/README.md">R<sup>1</sup> and R<sup>d</sup> Vector/Tensor Spaces (Validated and/or Normed), and Function Classes</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/spaces/big/README.md">Big-data In-place Manipulator</a></li>
 *  </ul>
 * <br><br>
 *
 * @author Lakshmi Krishnamurthy
 */

public class MoviesInFlight
{
	private int _travelTime = -1;
	private int _adjustmentTime = -1;
	private int[] _movieDurationArray = null;

	/**
	 * MoviesInFlight Constructor
	 * 
	 * @param movieDurationArray Movie Duration Array
	 * @param travelTime Travel Time
	 * @param adjustmentTime Adjustment Time
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public MoviesInFlight (
		final int[] movieDurationArray,
		final int travelTime,
		final int adjustmentTime)
		throws java.lang.Exception
	{
		if (null == (_movieDurationArray = movieDurationArray) ||
			0 >= (_travelTime = travelTime) ||
			-1 >= (_adjustmentTime = adjustmentTime) || _adjustmentTime >= _travelTime
		)
		{
			throw new java.lang.Exception (
				"MoviesInFlight Constructor => Invalid Inputs"
			);
		}

		int movieCount = _movieDurationArray.length;

		if (0 == movieCount)
		{
			{
				throw new java.lang.Exception (
					"MoviesInFlight Constructor => Invalid Inputs"
				);
			}
		}

		for (int movieIndex = 0;
			movieIndex < movieCount;
			++movieIndex)
		{
			if (0 >= _movieDurationArray[movieIndex])
			{
				{
					throw new java.lang.Exception (
						"MoviesInFlight Constructor => Invalid Inputs"
					);
				}
			}
		}
	}

	/**
	 * Retrieve the Array of Movie Duration
	 * 
	 * @return Array of Movie Duration
	 */

	public  int[] movieDurationArray()
	{
		return _movieDurationArray;
	}

	/**
	 * Retrieve the Travel Time
	 * 
	 * @return The Travel Time
	 */

	public int travelTime()
	{
		return _travelTime;
	}

	/**
	 * Retrieve the Adjustment Time
	 * 
	 * @return The Adjustment Time
	 */

	public int adjustmentTime()
	{
		return _adjustmentTime;
	}

	/**
	 * Retrieve the Movie Pair Meeting the Closest Lower Time Criterion
	 * 
	 * @return The Movie Pair Meeting the Closest Lower Time Criterion
	 */

	public int[] moviePair()
	{
		int[] moviePair = new int[2];
		int longestTotalDuration = 0;
		int lowerMovieDurationIndex = 0;
		int movieCount = _movieDurationArray.length;
		int upperMovieDurationIndex = movieCount - 1;
		int adjustedTravelTime = _travelTime - _adjustmentTime;
		java.lang.Integer[] sortedMovieDurationArray = new java.lang.Integer[movieCount];

		java.util.TreeMap<java.lang.Integer, java.lang.Integer> movieDurationTreeMap =
			new java.util.TreeMap<java.lang.Integer, java.lang.Integer>();

		for (int movieIndex = 0;
			movieIndex < movieCount;
			++movieIndex)
		{
			movieDurationTreeMap.put (
				_movieDurationArray[movieIndex],
				movieIndex
			);
		}

		movieDurationTreeMap.keySet().toArray (
			sortedMovieDurationArray
		);

		while (lowerMovieDurationIndex <= upperMovieDurationIndex)
		{
			int lowerDuration = sortedMovieDurationArray[lowerMovieDurationIndex];

			int upperDuration = sortedMovieDurationArray[upperMovieDurationIndex];

			int totalDuration = lowerDuration + upperDuration;

			if (totalDuration < adjustedTravelTime)
			{
				if (0 == longestTotalDuration)
				{
					longestTotalDuration = totalDuration;
					moviePair[1] = upperDuration;
					moviePair[0] = lowerDuration;
				}
				else
				{
					if (totalDuration > longestTotalDuration)
					{
						longestTotalDuration = totalDuration;
						moviePair[1] = upperDuration;
						moviePair[0] = lowerDuration;
					}
				}
			}
			else
			{
				--upperMovieDurationIndex;
			}

			++lowerMovieDurationIndex;
		}

		return moviePair;
	}
}
