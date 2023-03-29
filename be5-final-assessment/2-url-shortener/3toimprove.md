### to improve

- triggers should also be in migrations, shouldn't be versions - should be sequence of migrations
- when using migrations -> `spring.session.jdbc.initialize-schema=always` should be turned off
- `StaticResourcesConfig.java` and `WebMvcConfig.java` could be in one file, since they both implements one interface
- it's better to have more sophisticated error handling
- what is the convention to have field and table names in CAPS?
  - `@Table(name = "URL")`
  - `@Column(name = "SHORT_URL")`
- `@Data` instead `Getter Setter AllArgs NoArgs ...`
- for shortening, `random` is good, but it's better to use something hash based.
- 