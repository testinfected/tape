package com.vtence.tape.support;

public class TestEnvironment {

    public final String url;
    public final String username;
    public final String password;

    public TestEnvironment(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static TestEnvironment memory() {
        return new TestEnvironment("jdbc:h2:mem:test", "tape", "test");
    }
}
