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

package com.webauthn4j;

import com.webauthn4j.response.WebAuthnRegistrationContext;
import com.webauthn4j.response.client.ClientDataType;
import com.webauthn4j.converter.AttestationObjectConverter;
import com.webauthn4j.converter.CollectedClientDataConverter;
import com.webauthn4j.registry.Registry;
import com.webauthn4j.server.ServerProperty;
import org.junit.Test;

import static com.webauthn4j.test.TestUtil.createAttestationObjectWithFIDOU2FAttestationStatement;
import static com.webauthn4j.test.TestUtil.createClientData;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class WebAuthnRegistrationContextTest {

    private Registry registry = new Registry();

    @Test
    public void test() {
        byte[] collectedClientData = new CollectedClientDataConverter(registry).convertToBytes(createClientData(ClientDataType.GET));
        byte[] authenticatorData = new AttestationObjectConverter(registry).convertToBytes(createAttestationObjectWithFIDOU2FAttestationStatement());

        ServerProperty serverProperty = mock(ServerProperty.class);

        WebAuthnRegistrationContext webAuthnRegistrationContextA = new WebAuthnRegistrationContext(collectedClientData, authenticatorData, serverProperty, false);
        WebAuthnRegistrationContext webAuthnRegistrationContextB = new WebAuthnRegistrationContext(collectedClientData, authenticatorData, serverProperty, false);

        assertThat(webAuthnRegistrationContextA).isEqualTo(webAuthnRegistrationContextB);
        assertThat(webAuthnRegistrationContextA).hasSameHashCodeAs(webAuthnRegistrationContextB);
    }

}
