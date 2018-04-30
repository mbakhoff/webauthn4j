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

package com.webauthn4j.converter;

import com.webauthn4j.attestation.AttestationObject;
import com.webauthn4j.attestation.authenticator.AuthenticatorData;
import com.webauthn4j.attestation.statement.FIDOU2FAttestationStatement;
import com.webauthn4j.attestation.statement.AttestationStatement;
import com.webauthn4j.test.TestUtil;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AttestationObjectConverterTest {

    private AttestationObjectConverter target = new AttestationObjectConverter();

    @Ignore
    @Test
    public void convert_deserialization_test() {
        String testData = "o2hhdXRoRGF0YVkBLEmWDeWIDoxodDQXD2R2YFuP5K65ooYyx5lc87qDHZdjQQAAAAAAAAAAAAAAAAAAAAAAAAAAAKIACP59QEXMiaemII_WZWgoIBmmkh7u5xvxoHPRL-RV5NmOWCp9_fTzb8OSLyJC-digpP3ca_tDREm1wSwAo7-Q-WTI_PZ4D7dpj8JxNm3ewHpLpo42QSiRb1joquwVFg13fp9S4-uYiUp-pSozyH_tghNpcOqJ-riHuXu2kLR5Cr2XBa7IQpswofjHL57GQkxvOvifcwaD2gEYWoAMY0N9vn2jY2FsZ2VFUzI1NmF4WCBrz3D7ICYMH0jEkDdGip-1kNA-dzRbNsoxUuAbmiOczmF5WCBpN2_GpRfY3MKJRPE0gTLFdmUJlahghRNeb4rV8sdtrWNmbXRoZmlkby11MmZnYXR0U3RtdKJjeDVjgVkBNzCCATMwgdmgAwIBAgIFAIIN5C0wCgYIKoZIzj0EAwIwITEfMB0GA1UEAxMWRmlyZWZveCBVMkYgU29mdCBUb2tlbjAeFw0xNzA4MDYxMzQ3NTBaFw0xNzA4MDgxMzQ3NTBaMCExHzAdBgNVBAMTFkZpcmVmb3ggVTJGIFNvZnQgVG9rZW4wWTATBgcqhkjOPQIBBggqhkjOPQMBBwNCAATt8WX-ju7pwFW1dsU63HaoCJdkZQ1ZrMTmq5egYzkqirtpq7BAAf-2sgWHJA_AhpTy56kfrEx0csiAU-Mvj6oqMAoGCCqGSM49BAMCA0kAMEYCIQCBOXuTIst0TswK3mHn34VOEG_2Py5bWOt3PQtWXH6d9AIhALDVZWNPvukW9eniDcWZ-MMSq4C5V98UrcUW9d49zroEY3NpZ1hHMEUCIQDj2wuWgR6Rz8jvWYjsqZt_Va5FUl4POFuPehYAXeG-oQIgFGIm73KFf_lKqv8KVxpJb_IWqJTF3i97wTo3UjfJImk";
        AttestationObject attestationObject = target.convert(testData);
        AuthenticatorData authenticatorData = attestationObject.getAuthenticatorData();
        String format = attestationObject.getFormat();
        AttestationStatement attestationStatement = attestationObject.getAttestationStatement();

        assertThat(authenticatorData).isNotNull();
        assertThat(format).isEqualTo("fido-u2f");
        assertThat(attestationStatement).isInstanceOf(FIDOU2FAttestationStatement.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void convert_test_with_illegal_input() {
        String testData = "illegal input";
        target.convert(testData);
    }

    @Test
    public void convert_serialization_test() {
        AttestationObject input = TestUtil.createWebAuthnAttestationObjectWithFIDOU2FAttestationStatement();
        String result = target.convertToString(input);
        AttestationObject deserialized = target.convert(result);
        assertThat(deserialized).isEqualTo(input);
    }


}