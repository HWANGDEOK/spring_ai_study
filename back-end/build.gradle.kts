plugins {
	java
	id("org.springframework.boot") version "4.1.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.hyeondeok"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

extra["springAiVersion"] = "2.0.1"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springframework.ai:spring-ai-starter-model-openai")
	implementation("org.springframework.ai:spring-ai-starter-model-ollama:2.0.1")
	// JdbcChatMemoryRepository 클래스와 자동 구성 로직이 포함된 스타터
	implementation("org.springframework.ai:spring-ai-starter-model-chat-memory-repository-jdbc:2.0.1")
	// MySQL 연결을 위한 JDBC 드라이버
	runtimeOnly("com.mysql:mysql-connector-j:26.7.0")


	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testCompileOnly("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testAnnotationProcessor("org.projectlombok:lombok")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
