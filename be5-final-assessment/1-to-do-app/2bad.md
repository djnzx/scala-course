### bad

- `hibernate.ddl-auto: update` is bad, migrations FlyWay / Liquibase should be used
- `HtmlUtil.java` has just inlined HTML which is not good. Should be different files in the resources folder
- `EmailProvider.java` has inlined templates for messages. Should be different files in the resources folder
- lack of `Optional` type to represent failed cases
- `SecurityConstants.java` secret keys shouldn't be in source code
- `JavaMailSenderConfig.java` credentials in the source code