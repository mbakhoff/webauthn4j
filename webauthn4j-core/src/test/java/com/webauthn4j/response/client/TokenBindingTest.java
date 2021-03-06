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

package com.webauthn4j.response.client;

import com.webauthn4j.util.Base64UrlUtil;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenBindingTest {

    @Test
    public void constructor_test() {
        TokenBinding tokenBindingA = new TokenBinding(TokenBindingStatus.SUPPORTED, Base64UrlUtil.encodeToString(new byte[]{0x01, 0x23, 0x45}));
        TokenBinding tokenBindingB = new TokenBinding(TokenBindingStatus.SUPPORTED, new byte[]{0x01, 0x23, 0x45});

        assertThat(tokenBindingA).isEqualTo(tokenBindingB);
    }

    @Test
    public void equals_hashCode_test() {
        TokenBinding tokenBindingA = new TokenBinding(TokenBindingStatus.SUPPORTED, new byte[]{0x01, 0x23, 0x45});
        TokenBinding tokenBindingB = new TokenBinding(TokenBindingStatus.SUPPORTED, new byte[]{0x01, 0x23, 0x45});

        assertThat(tokenBindingA).isEqualTo(tokenBindingB);
        assertThat(tokenBindingA).hasSameHashCodeAs(tokenBindingB);
    }


}
