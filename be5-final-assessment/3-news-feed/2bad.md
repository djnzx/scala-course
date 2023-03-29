### bad

- local database
- no migrations
- no deployment
- `UserService` public variable `Map<String, Object> forToken = new HashMap<>();` ???
- `HTML` inlined in the java code
- `ArticlesController` - `private static List<Website> disabled = new ArrayList<>();`
  - why static
  - why mutable variable on the controller
  - why not HashMap!
- how would you map `private List<String> roles = new ArrayList<>();` as a basic attribute?
- no dto / mapStruct / model mapper
- expect to see `ArticleServiceImpl` has list of `JsoupParser` not just links to each implementation
- `TechStartupsParser`. why `List<Article> articles = new ArrayList<>();` declared on the class, not method?