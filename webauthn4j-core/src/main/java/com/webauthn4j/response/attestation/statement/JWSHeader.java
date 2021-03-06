/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webauthn4j.response.attestation.statement;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class JWSHeader implements Serializable {

    @JsonProperty
    private String alg;

    @JsonProperty
    private AttestationCertificatePath x5c;

    public JWSHeader(){
        //nop
    }

    public String getAlg() {
        return alg;
    }

    public AttestationCertificatePath getX5c() {
        return x5c;
    }
}
