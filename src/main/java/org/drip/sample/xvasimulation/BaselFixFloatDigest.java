
package org.drip.sample.xvasimulation;

import org.drip.analytics.date.*;
import org.drip.measure.dynamics.*;
import org.drip.measure.process.DiffusionEvolver;
import org.drip.measure.statistics.UnivariateDiscreteThin;
import org.drip.quant.common.FormatUtil;
import org.drip.service.env.EnvManager;
import org.drip.xva.cpty.*;
import org.drip.xva.dynamics.*;
import org.drip.xva.set.*;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * 
 *  This file is part of DRIP, a free-software/open-source library for buy/side financial/trading model
 *  	libraries targeting analysts and developers
 *  	https://lakshmidrip.github.io/DRIP/
 *  
 *  DRIP is composed of four main libraries:
 *  
 *  - DRIP Fixed Income - https://lakshmidrip.github.io/DRIP-Fixed-Income/
 *  - DRIP Asset Allocation - https://lakshmidrip.github.io/DRIP-Asset-Allocation/
 *  - DRIP Numerical Optimizer - https://lakshmidrip.github.io/DRIP-Numerical-Optimizer/
 *  - DRIP Statistical Learning - https://lakshmidrip.github.io/DRIP-Statistical-Learning/
 * 
 *  - DRIP Fixed Income: Library for Instrument/Trading Conventions, Treasury Futures/Options,
 *  	Funding/Forward/Overnight Curves, Multi-Curve Construction/Valuation, Collateral Valuation and XVA
 *  	Metric Generation, Calibration and Hedge Attributions, Statistical Curve Construction, Bond RV
 *  	Metrics, Stochastic Evolution and Option Pricing, Interest Rate Dynamics and Option Pricing, LMM
 *  	Extensions/Calibrations/Greeks, Algorithmic Differentiation, and Asset Backed Models and Analytics.
 * 
 *  - DRIP Asset Allocation: Library for model libraries for MPT framework, Black Litterman Strategy
 *  	Incorporator, Holdings Constraint, and Transaction Costs.
 * 
 *  - DRIP Numerical Optimizer: Library for Numerical Optimization and Spline Functionality.
 * 
 *  - DRIP Statistical Learning: Library for Statistical Evaluation and Machine Learning.
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
 * BaselFixFloatDigest simulates for various Latent States and Exposures for an Fix Float Swap and computes
 *  the XVA Metrics using the Basel Proxy-Style Fading Swap Volatility. The References are:
 *  
 *  - Burgard, C., and M. Kjaer (2014): PDE Representations of Derivatives with Bilateral Counter-party Risk
 *  	and Funding Costs, Journal of Credit Risk, 7 (3) 1-19.
 *  
 *  - Burgard, C., and M. Kjaer (2014): In the Balance, Risk, 24 (11) 72-75.
 *  
 *  - Albanese, C., and L. Andersen (2014): Accounting for OTC Derivatives: Funding Adjustments and the
 *  	Re-Hypothecation Option, eSSRN, https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2482955.
 *  
 *  - Burgard, C., and M. Kjaer (2017): Derivatives Funding, Netting, and Accounting, eSSRN,
 *  	https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2534011.
 * 
 *  - Piterbarg, V. (2010): Funding Beyond Discounting: Collateral Agreements and Derivatives Pricing, Risk
 *  	21 (2) 97-102.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class BaselFixFloatDigest
{

	private static final DiffusionSettings GenerateDiffusionSettings (
		final double dblTimeHorizon)
		throws Exception
	{
		double dblATMSwapRateVolatility = 0.25;
		double dblOvernightNumeraireDrift = 0.01;
		double dblOvernightNumeraireVolatility = 0.05;
		double dblCSADrift = 0.01;
		double dblCSAVolatility = 0.05;
		double dblBankHazardRateDrift = 0.002;
		double dblBankHazardRateVolatility = 0.20;
		double dblBankRecoveryRateDrift = 0.002;
		double dblBankRecoveryRateVolatility = 0.02;
		double dblBankFundingSpreadDrift = 0.00002;
		double dblBankFundingSpreadVolatility = 0.002;
		double dblCounterPartyHazardRateDrift = 0.002;
		double dblCounterPartyHazardRateVolatility = 0.30;
		double dblCounterPartyRecoveryRateDrift = 0.002;
		double dblCounterPartyRecoveryRateVolatility = 0.02;
		double dblCounterPartyFundingSpreadDrift = 0.00002;
		double dblCounterPartyFundingSpreadVolatility = 0.002;

		return new DiffusionSettings (
			new DiffusionEvolver (
				DiffusionEvaluatorLinearFader.Standard (
					0.,
					dblATMSwapRateVolatility,
					dblTimeHorizon
				)
			),
			new DiffusionEvolver (
				DiffusionEvaluatorLogarithmic.Standard (
					dblOvernightNumeraireDrift,
					dblOvernightNumeraireVolatility
				)
			),
			new DiffusionEvolver (
				DiffusionEvaluatorLogarithmic.Standard (
					dblCSADrift,
					dblCSAVolatility
				)
			),
			new DiffusionEvolver (
				DiffusionEvaluatorLogarithmic.Standard (
					dblBankHazardRateDrift,
					dblBankHazardRateVolatility
				)
			),
			new DiffusionEvolver (
				DiffusionEvaluatorLogarithmic.Standard (
					dblBankRecoveryRateDrift,
					dblBankRecoveryRateVolatility
				)
			),
			new DiffusionEvolver (
				DiffusionEvaluatorLinear.Standard (
					dblBankFundingSpreadDrift,
					dblBankFundingSpreadVolatility
				)
			),
			new DiffusionEvolver (
				DiffusionEvaluatorLogarithmic.Standard (
					dblCounterPartyHazardRateDrift,
					dblCounterPartyHazardRateVolatility
				)
			),
			new DiffusionEvolver (
				DiffusionEvaluatorLogarithmic.Standard (
					dblCounterPartyRecoveryRateDrift,
					dblCounterPartyRecoveryRateVolatility
				)
			),
			new DiffusionEvolver (
				DiffusionEvaluatorLinear.Standard (
					dblCounterPartyFundingSpreadDrift,
					dblCounterPartyFundingSpreadVolatility
				)
			),
			new double[][] {
				{1.00,  0.00,  0.03,  0.07,  0.04,  0.05,  0.08,  0.00,  0.00},  // PORTFOLIO
				{0.00,  1.00,  0.00,  0.00,  0.00,  0.00,  0.00,  0.00,  1.00},  // OVERNIGHT
				{0.03,  0.00,  1.00,  0.26,  0.33,  0.21,  0.35,  0.13,  0.00},  // CSA
				{0.07,  0.00,  0.26,  1.00,  0.45, -0.17,  0.07,  0.77,  0.00},  // BANK HAZARD
				{0.04,  0.00,  0.33,  0.45,  1.00, -0.22, -0.54,  0.58,  0.00},  // COUNTER PARTY HAZARD
				{0.05,  0.00,  0.21, -0.17, -0.22,  1.00,  0.47, -0.23,  0.00},  // BANK RECOVERY
				{0.08,  0.00,  0.35,  0.07, -0.54,  0.47,  1.00,  0.01,  0.00},  // COUNTER PARTY RECOVERY
				{0.00,  0.00,  0.13,  0.77,  0.58, -0.23,  0.01,  1.00,  0.00},  // BANK FUNDING SPREAD
				{0.00,  0.00,  0.00,  0.00,  0.00,  0.00,  0.00,  0.00,  1.00}   // COUNTER PARTY FUNDING SPREAD
			}
		);
	}

	private static final void UDTDump (
		final String strHeader,
		final JulianDate[] adtVertexNode,
		final UnivariateDiscreteThin[] aUDT)
		throws Exception
	{
		System.out.println ("\t|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");

		System.out.println (strHeader);

		System.out.println ("\t|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");

		String strDump = "\t|       DATE      =>" ;

		for (int i = 0; i < adtVertexNode.length; ++i)
			strDump = strDump + " " + adtVertexNode[i] + "  |";

		System.out.println (strDump);

		System.out.println ("\t|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");

		 strDump = "\t|     AVERAGE     =>";

		for (int j = 0; j < aUDT.length; ++j)
			strDump = strDump + "   " + FormatUtil.FormatDouble (aUDT[j].average(), 2, 4, 1.) + "   |";

		System.out.println (strDump);

		strDump = "\t|     MAXIMUM     =>";

		for (int j = 0; j < aUDT.length; ++j)
			strDump = strDump + "   " + FormatUtil.FormatDouble (aUDT[j].maximum(), 2, 4, 1.) + "   |";

		System.out.println (strDump);

		strDump = "\t|     MINIMUM     =>";

		for (int j = 0; j < aUDT.length; ++j)
			strDump = strDump + "   " + FormatUtil.FormatDouble (aUDT[j].minimum(), 2, 4, 1.) + "   |";

		System.out.println (strDump);

		strDump = "\t|      ERROR      =>";

		for (int j = 0; j < aUDT.length; ++j)
			strDump = strDump + "   " + FormatUtil.FormatDouble (aUDT[j].error(), 2, 4, 1.) + "   |";

		System.out.println (strDump);

		System.out.println ("\t|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");
	}

	private static final void UDTDump (
		final String strHeader,
		final UnivariateDiscreteThin udt)
		throws Exception
	{
		System.out.println (
			strHeader +
			FormatUtil.FormatDouble (udt.average(), 3, 2, 100.) + "% | " +
			FormatUtil.FormatDouble (udt.maximum(), 3, 2, 100.) + "% | " +
			FormatUtil.FormatDouble (udt.minimum(), 3, 2, 100.) + "% | " +
			FormatUtil.FormatDouble (udt.error(), 3, 2, 100.) + "% ||"
		);
	}

	public static final void main (
		final String[] astrArgs)
		throws Exception
	{
		EnvManager.InitEnv (
			"",
			true
		);

		/*
		 * Evolution Control
		 */

		int iNumPath = 100000;
		int iNumTimeSteps = 10;
		double dblTimeHorizon = 5.;

		JulianDate dtSpot = DateUtil.Today();

		EvolutionControl fixFloatEvolutionControl = new EvolutionControl (
			dtSpot,
			dblTimeHorizon,
			iNumTimeSteps
		);

		/*
		 * Group Settings
		 */

		double dblBankThreshold = -0.1;
		double dblCounterPartyThreshold = 0.1;

		GroupSettings fixFloatGroupSettings = new GroupSettings (
			CollateralGroupSpecification.FixedThreshold (
				"FIXEDTHRESHOLD",
				dblCounterPartyThreshold,
				dblBankThreshold
			),
			CounterPartyGroupSpecification.Standard ("CPGROUP")
		);

		DiffusionSettings fixFloatDiffusionSettings = GenerateDiffusionSettings (dblTimeHorizon);

		PathSimulator fixFloatPathSimulator = new PathSimulator (
			iNumPath,
			fixFloatGroupSettings,
			fixFloatDiffusionSettings,
			fixFloatEvolutionControl
		);

		StateEntityRealization initialStateEntityRealization = new StateEntityRealization (
			0.000, 				// dblPortfolioValueInitial
			1.000, 				// dblOvernightNumeraireInitial
			1.000, 				// dblCSANumeraire
			0.015, 				// dblBankHazardRate
			0.400, 				// dblBankRecoveryRate
			0.015 / (1 - 0.40), // dblBankFundingSpread
			0.030, 				// dblCounterPartyHazardRate
			0.300, 				// dblCounterPartyRecoveryRate
			0.030 / (1 - 0.30) 	// dblCounterPartyFundingSpread
		);

		ExposureAdjustmentAggregator eaa = fixFloatPathSimulator.simulate (initialStateEntityRealization);

		ExposureAdjustmentDigest ead = eaa.digest();

		System.out.println();

		UDTDump (
			"\t|                                                                                COLLATERALIZED EXPOSURE                                                                                |",
			eaa.anchors(),
			ead.collateralizedExposure()
		);

		UDTDump (
			"\t|                                                                               UNCOLLATERALIZED EXPOSURE                                                                               |",
			eaa.anchors(),
			ead.uncollateralizedExposure()
		);

		UDTDump (
			"\t|                                                                                COLLATERALIZED EXPOSURE PV                                                                             |",
			eaa.anchors(),
			ead.collateralizedExposurePV()
		);

		UDTDump (
			"\t|                                                                               UNCOLLATERALIZED EXPOSURE PV                                                                            |",
			eaa.anchors(),
			ead.uncollateralizedExposurePV()
		);

		UDTDump (
			"\t|                                                                            COLLATERALIZED POSITIVE EXPOSURE PV                                                                        |",
			eaa.anchors(),
			ead.collateralizedPositiveExposure()
		);

		UDTDump (
			"\t|                                                                           UNCOLLATERALIZED POSITIVE EXPOSURE PV                                                                       |",
			eaa.anchors(),
			ead.uncollateralizedPositiveExposure()
		);

		UDTDump (
			"\t|                                                                            COLLATERALIZED NEGATIVE EXPOSURE PV                                                                        |",
			eaa.anchors(),
			ead.collateralizedNegativeExposure()
		);

		UDTDump (
			"\t|                                                                           UNCOLLATERALIZED NEGATIVE EXPOSURE PV                                                                       |",
			eaa.anchors(),
			ead.uncollateralizedNegativeExposure()
		);

		System.out.println();

		System.out.println ("\t||-----------------------------------------------------||");

		System.out.println ("\t||  UCVA CVA FTDCVA DVA FCA UNIVARIATE THIN STATISTICS ||");

		System.out.println ("\t||-----------------------------------------------------||");

		System.out.println ("\t||    L -> R:                                          ||");

		System.out.println ("\t||            - Path Average                           ||");

		System.out.println ("\t||            - Path Maximum                           ||");

		System.out.println ("\t||            - Path Minimum                           ||");

		System.out.println ("\t||            - Monte Carlo Error                      ||");

		System.out.println ("\t||-----------------------------------------------------||");

		UDTDump (
			"\t||  UCVA  => ",
			ead.ucva()
		);

		UDTDump (
			"\t|| FTDCVA => ",
			ead.ftdcva()
		);

		UDTDump (
			"\t||   CVA  => ",
			ead.cva()
		);

		UDTDump (
			"\t||  CVACL => ",
			ead.cvacl()
		);

		UDTDump (
			"\t||   DVA  => ",
			ead.dva()
		);

		UDTDump (
			"\t||   FVA  => ",
			ead.fva()
		);

		UDTDump (
			"\t||   FDA  => ",
			ead.fda()
		);

		UDTDump (
			"\t||   FCA  => ",
			ead.fca()
		);

		UDTDump (
			"\t||   FBA  => ",
			ead.fba()
		);

		UDTDump (
			"\t||  SFVA  => ",
			ead.sfva()
		);

		System.out.println ("\t||-----------------------------------------------------||");

		UDTDump (
			"\t||  Total => ",
			ead.totalVA()
		);

		System.out.println ("\t||-----------------------------------------------------||");

		EnvManager.TerminateEnv();
	}
}
