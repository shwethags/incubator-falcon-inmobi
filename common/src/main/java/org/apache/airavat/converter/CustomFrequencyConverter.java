/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.airavat.converter;

import org.apache.airavat.entity.v0.ProcessType;
import org.apache.airavat.oozie.coordinator.COORDINATORAPP;
import org.dozer.DozerConverter;

public class CustomFrequencyConverter extends
		DozerConverter<ProcessType, COORDINATORAPP> {

	public CustomFrequencyConverter() {
		super(ProcessType.class, COORDINATORAPP.class);
	}

	@Override
	public COORDINATORAPP convertTo(ProcessType processType,
			COORDINATORAPP coordinatorapp) {

		coordinatorapp.setFrequency("${coord:" + processType.getFrequency()
				+ "(" + processType.getPeriodicity() + ")}");

		return coordinatorapp;
	}

	@Override
	public ProcessType convertFrom(COORDINATORAPP arg0, ProcessType arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}