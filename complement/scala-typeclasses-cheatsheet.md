### Basic Monad Combinators (`F[A]`: `Option[A]`, `Either[E, A]`, `IO[A]`, etc)
- `F[_]: Monad` required

| Having    | Combinator | Applying       | Getting |
|-----------|------------|----------------|---------|
| `F[A]`    | `.map`     | `f: A => B`    | `F[B]`  |
| `F[A]`    | `.flatMap` | `f: A => F[B]` | `F[B]`  |
| `F[F[A]]` | `.flatten` |                | `F[A]`  |

### Any Coproduct Combinators (`Option[A]`, `Either[E, A]`)

| Having         | Combinator | Applying                    | Getting |
|----------------|------------|-----------------------------|---------|
| `Option[A]`    | `.fold`    | `f: None => B`, `g: A => B` | `B`     |
| `Either[E, A]` | `.fold`    | `f: E => B`, `g: A => B`    | `B`     |

### Traversable Combinators (`F[A]`, `List[A]`, `Vector[A]`, etc)
- `F[_]: Applicative`, `G[_]: Applicative` required

| Having       | Combinator      | Applying          | Getting   |
|--------------|-----------------|-------------------|-----------|
| `F[A]`       | `.traverse`     | `f: A => G[B]`    | `G[F[B]]` |
| `F[A]`       | `.traverseTap`  | `f: A => G[B]`    | `G[F[A]]` |
| `F[G[A]]`    | `.sequence`     |                   | `G[F[A]]` |
| `F[A]`       | `.flatTraverse` | `f: A => G[F[B]]` | `G[F[B]]` |
| `F[G[F[A]]]` | `.flatSequence` |                   | `G[F[A]]` |

### Foldable Combinators (`F[A]`, `List[A]`, `Vector[A]`, etc)
- `Applicative[F]` required

| Having | Combinator  | Applying                 | Getting |                  |
|--------|-------------|--------------------------|---------|------------------|
| `F[A]` | `.foldLeft` | `B`, `f: (B, A) => B`    | `B`     |                  |
| `F[A]` | `.fold`     |                          | `A`     | `Monoid[A]`      |
| `F[A]` | `.foldMap`  | `f: A => B`              | `B`     | `Monoid[B]`      |
| `F[A]` | `.foldM`    | `B`, `f: (B, A) => G[B]` | `G[B]`  | `Monad[G]`       |
| `F[A]` | `.foldA`    | `B`, `f: (B, A) => G[B]` | `G[B]`  | `Applicative[G]` |
| `F[A]` | `.foldA`    | `f: A => G[B]`           | `G[B]`  |                  |

### OptionT Combinators (`OptionT[F, A]`)

| Having          | Combinator | Applying | Getting |
|-----------------|------------|----------|---------|
| `OptionT[F, A]` | ``         | ``       | ``      |
| `OptionT[F, A]` | ``         | ``       | ``      |
|                 |            |          |         |

### EitherT Combinators (`EitherT[F, E, A]`)

| Having             | Combinator | Applying | Getting |
|--------------------|------------|----------|---------|
| `EitherT[F, E, A]` | ``         | ``       | ``      |
| `EitherT[F, E, A]` | ``         | ``       | ``      |

