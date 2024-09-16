<!-- Copyright (c) 2024 Tobias Briones. All rights reserved. -->
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- This file is part of https://github.com/tobiasbriones/blog -->

# In-House MathSwe-TS-and-Client Libraries | MathSwe Ops Services (2024/09/15)

It provides two new in-house libraries in the MathSwe Ops Services application
that will become MathSwe standard libraries for any TypeScript and server
projects. While MathSwe-TS leverages the FP-TS library to add extended support
for FP, MathSwe-Client will be standard for server applications.

---

**Add in-house libs mathswe-ts, mathswe-client**

Sep 15: PR [#2](https://github.com/mathswe-ops/services/pull/2) merged into
`services/dev <- services/mathswe-ts-and-client`
by [tobiasbriones](https://github.com/tobiasbriones)
{: .pr-subtitle }

The `mathswe-ts/adt` module defines the **general pattern matching for sum types
**. You also must follow manual guides to correctly build a sum type in
TypeScript, even with `fp-ts` and `mathswe-ts`.

`Sum Type Pattern Matching`

```ts

describe("Lazy Rich SumType", () => {
    type Shape = { tag: "Point" } | { tag: "Circle", radius: number };

    const point: Shape = { tag: "Point" };
    const circle: Shape = { tag: "Circle", radius: 1 };

    it("computes the area via matching and destructure", () => {
        type Circle = { radius: number };

        const circleArea
            = ({ radius }: Circle) => Math.PI * Math.pow(radius, 2);

        const area = (shape: Shape): number => {
            const withShapeVariant = withMatchVariant<number>(shape);

            return pipe(
                shape,
                match({
                    Point: () => 0,
                    Circle: withShapeVariant(circleArea),
                }),
            );
        };

        expect(area(point)).toBe(0);
        expect(area(circle)).toBe(Math.PI);
    });
});
```

Something the test doesn't show is that you must create the data constructors
manually to keep up with good practices. That is, to avoid the `tag` field in
client code, which is supposed to be an implementation detail.

Notice that, when using `pipe` you give context to the `match` function, i.e.
`SumTypeMap<Shape, string>`, so **the pattern matching is exhaustive**. If you
call it separately, i.e. `match({})(shape)`, it loses the sum type, i.e.
`SumTypeMap<SumType, string>`, so the generic type weakens from `Shape` to
`SumType`.

You must apply the `withMatchVariant` higher-order function to pass your branch
function to it. Your branch function, for example, `circleArea`, *will define
the type of the branch*. In that case, you tell your `withShapeVariant` partial
function that *the type of the branch argument is `Circle`* since it performs a
cast under the hood, so you must define this type correctly to match
successfully the branch.

As you can see, the branch function `circleArea` defines the `{ radius }:
Circle` param type that the `withShapeVariant` partial function will use to
convert the original sum type to the actual variant value.

Finally, the matching is lazy for correctness. If you pass an eager map to
`match`, it will execute all the variants automatically. Further, it would fail
at runtime because it'd pass the same argument to all the different branch
functions without discrimination.

Conversely, the `mathswe-ts/enum` module allows you to **match plain sum type
branches** eagerly.

`Matching Plain Sum Type Branches`

```ts
describe("PlainEnum", () => {
    type Color = { tag: "Red" } | { tag: "Green" } | { tag: "Blue" };

    const red: Color = { tag: "Red" };
    const green: Color = { tag: "Green" };
    const blue: Color = { tag: "Blue" };

    it("should pipe match enum variants with type safe exhaustive map", () => {
        const label = (color: Color) => pipe(
            color,
            matchPlain({
                Red: "red-variant",
                Green: "green-variant",
                Blue: "blue-variant",
            }),
        );

        expect(label(red)).toBe("red-variant");
        expect(label(green)).toBe("green-variant");
        expect(label(blue)).toBe("blue-variant");
    });
});
```

The `matchPlain` function of the `enum` module is eager, and **you must not use
`matchPlain` for non-trivial sum types**.

Notice that, `fp-ts` doesn't have a general pattern-matching solution, only
ad-hoc matching functions for specific types, like `Either`. Therefore, I took
the exciting challenge to solve it, as per the language limitations since it's
better to have some kind of pattern matching than none.

Finally, the `mathswe-ts/string` module defines some type-classes to start
implementing them in TS programs as a standard practice (like the Rust way).

`Type Classes in the String Module`

```ts
export interface ToString<T> {
    toString(value: T): string;
}

export interface FromString<T> {
    fromString(string: string): Either<string, T>;
}
```

The module `mathswe-ts/require` provides a method `requireRight` to unsafe
unwrap of `Either` values. It throws an `Error` if the given `Either` is `Left`,
or else returns its `Right` value. It's useful for testing when you're sure a
value is always `Right`, or must be `Right` to pass the test.

The current MathSwe-TS library supports sum-type general pattern matching, which
is lazy, but also for eager matching for plain branches without fields. It
further defines some type-classes for Strings and a method for unsafe unwrapping
of Either.

---

The `mathswe-client/domain/domain` module defines the `ToDomainName` type class
to convert any domain to a `string` domain name. It also defines the `Allowed`
sum type to grant access to those domains.

`Domain Module`

```ts
export interface ToDomainName<T> {
    toDomainName(domain: T): string;
}

export type Allowed
    = { tag: "FullAccess" }
    | { tag: "PartialAccess", values: string[] }
```

`MathSwe Module`

```ts
export type MathSwe
    = "MathSweCom"
"MathSoftware"
"MathSoftwareEngineer";
```

`ThirdParty Module`

```ts
export type ThirdParty = "GitHubCom";
```

The `mathswe-client/req/http` module defines HTTP abstractions.

`HTTP Module`

```ts
export type Hostname = {
    domainName: string,
    subdomain: string,
}

export type Path = string[];

export type SecureUrl = {
    hostname: Hostname,
    path: Path,
}
```

The `mathswe-client/req/origin` path contains modules with rules defining if a
given request origin has access to the server.

`Modules OriginDomain and Origin`

```ts
export type OriginDomain
    = { tag: "MathSweDomain", mathswe: MathSwe }
    | { tag: "ThirdPartyDomain", thirdParty: ThirdParty };

export type OriginPath = Path;

export type Origin = {
    domain: OriginDomain,
    path: OriginPath,
    url: SecureUrl,
}
```

`Ensuring Access to Allowed Origins Only`

```ts
describe("newOriginPathFromDomain", () => {
    it("should return path for FullAccess domain", () => {
        const domain: OriginDomain = pipe(
            "mathswe.com",
            originDomainFromString.fromString,
            requireRight,
        );

        const path = "/any-path";
        const expected = right(pipe(path, newPathFromString, requireRight));
        const result = pipe(path, newOriginPathFromDomain(domain));

        expect(result).toEqual(expected);
    });

    it("should return an error for a restricted domain path", () => {
        const domain: OriginDomain = pipe(
            "github.com",
            originDomainFromString.fromString,
            requireRight,
        );

        // github.com/restricted-path random user
        const path = "restricted-path";
        const expected = left(`Path ${ path } of domain ${ domain } is restricted.`);
        const result = pipe(path, newOriginPathFromDomain(domain));

        expect(result).toEqual(expected);
    });

    it("should accept partially allowed path", () => {
        const domain: OriginDomain = pipe(
            "github.com",
            originDomainFromString.fromString,
            requireRight,
        );

        // github.com/mathswe organization
        const path = "mathswe";
        const expected = right(pipe(path, newPathFromString, requireRight));
        const result = pipe(path, newOriginPathFromDomain(domain));

        expect(result).toEqual(expected);
    });
});

describe("newOriginFromUrl", () => {
    it(
        "should return a valid Origin when domain and path are allowed",
        () => {
            const expectedUrl: SecureUrl = pipe(
                "https://mathswe.com/valid-path",
                newUrlFromString,
                requireRight,
            );

            const result = newOriginFromUrl(expectedUrl);
            const { domain, path, url } = requireRight(result);
            const expectedDomain = mathSweDomain("MathSweCom");
            const expectedPath = pipe(
                "valid-path",
                newPathFromString,
                requireRight,
            );

            expect(domain).toEqual(expectedDomain);
            expect(path).toEqual(expectedPath);
            expect(url).toEqual(expectedUrl);
        },
    );

    it("should return an error if the domain is disallowed", () => {
        const result = newOriginFromString("example.com/some-path");

        expect(isLeft(result)).toBe(true);
    });
});
```

Finally, the `mathswe-client/req/client/client-req` module provides a function
to get the `Origin` of a `Request`. Recall that the existence of an `Origin`
value means the underlying request is allowed.

`Parsing HTTP Request to Allowed Origin`

```ts
describe("getOrigin", () => {
    it(
        "should return Right(Origin) when a valid Origin header is provided",
        () => {
            const mockRequest = {
                headers: {
                    get: (key: string) =>
                        key === "Origin"
                        ? "https://mathswe.com"
                        : null,
                },
            } as Request;

            const result = getOrigin(mockRequest);
            const expectedOrigin = pipe(
                "https://mathswe.com",
                newOriginFromString,
                requireRight,
            );

            expect(result).toEqual(right(expectedOrigin));
        },
    );
});
```

The current MathSwe-Client library defines known MathSwe domains with full
access and third-party domains, like GitHub, which can only have partial access
to some paths. It defines HTTP concepts, like SecureUrl, so that DSL ensures
only allowed origins according to domain rules.

---

The new features of MathSwe-TS complement general and exhausting (when piping)
pattern matching for sum types. While it's not perfect, it works better than
expected, complimenting FP-TS, which only has ad-hoc matching for monads like
Option and Either.

Further, the new MathSwe-Client supports Origin rules to only accept HTTP
requests from accepted domains and paths.

The purpose of these two libraries is to become MathSwe standards for TypeScript
projects.
