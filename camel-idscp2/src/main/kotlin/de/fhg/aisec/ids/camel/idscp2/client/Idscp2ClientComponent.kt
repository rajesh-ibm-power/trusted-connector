/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.fhg.aisec.ids.camel.idscp2.client

import de.fhg.aisec.ids.idscp2.default_drivers.rat.dummy.RatProverDummy
import de.fhg.aisec.ids.idscp2.default_drivers.rat.dummy.RatVerifierDummy
import de.fhg.aisec.ids.idscp2.default_drivers.rat.tpm2d.TPM2dProver
import de.fhg.aisec.ids.idscp2.default_drivers.rat.tpm2d.TPM2dProverConfig
import de.fhg.aisec.ids.idscp2.default_drivers.rat.tpm2d.TPM2dVerifier
import de.fhg.aisec.ids.idscp2.default_drivers.rat.tpm2d.TPM2dVerifierConfig
import de.fhg.aisec.ids.idscp2.idscp_core.rat_registry.RatProverDriverRegistry
import de.fhg.aisec.ids.idscp2.idscp_core.rat_registry.RatVerifierDriverRegistry
import org.apache.camel.Endpoint
import org.apache.camel.spi.annotations.Component
import org.apache.camel.support.DefaultComponent

@Component("idscp2server")
class Idscp2ClientComponent : DefaultComponent() {

    init {
        RatProverDriverRegistry.registerDriver(
                RatProverDummy.RAT_PROVER_DUMMY_ID, ::RatProverDummy, null)
        RatVerifierDriverRegistry.registerDriver(
                RatVerifierDummy.RAT_VERIFIER_DUMMY_ID, ::RatVerifierDummy, null)
        RatProverDriverRegistry.registerDriver(
                TPM2dProver.TPM_RAT_PROVER_ID, ::TPM2dProver,
                TPM2dProverConfig.Builder().build()
        )
        RatVerifierDriverRegistry.registerDriver(
                TPM2dVerifier.TPM_RAT_VERIFIER_ID, ::TPM2dVerifier,
                TPM2dVerifierConfig.Builder().build()
        )
    }

    override fun createEndpoint(uri: String, remaining: String, parameters: Map<String, Any>): Endpoint {
        val endpoint: Endpoint = Idscp2ClientEndpoint(uri, remaining, this)
        setProperties(endpoint, parameters)
        return endpoint
    }

}