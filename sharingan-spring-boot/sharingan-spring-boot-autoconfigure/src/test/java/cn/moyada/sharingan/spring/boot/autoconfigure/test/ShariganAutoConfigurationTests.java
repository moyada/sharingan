/*
 * Copyright (C) 2018 the original author or authors.
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

package cn.moyada.sharingan.spring.boot.autoconfigure.test;

import cn.moyada.sharingan.spring.boot.autoconfigure.SharinganMonitorAutoConfiguration;
import cn.moyada.sharingan.spring.boot.autoconfigure.config.SharinganConfig;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class ShariganAutoConfigurationTests {

	private ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(SharinganMonitorAutoConfiguration.class))
			.withPropertyValues("sharingan.enable=true")
			.withPropertyValues("sharingan.application=test")
			;

	@Test
	public void testProperties() {
		this.contextRunner.run(context -> {
			SharinganConfig sharinganConfig = context.getBean(SharinganConfig.class);
			assertThat(sharinganConfig.getApplication()).isEqualTo("test");
			assertThat(sharinganConfig.isEnable()).isTrue();
		});
	}
}
