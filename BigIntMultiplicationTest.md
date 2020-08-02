#### Multiplication ([`BigIntMultiplicationTest`][BigIntMultiplicationTest])

**Summary**

```
[runtime performance]
╔══════════════════╦═════════╦═══════════╗
║                  ║   MPN   ║  BigInt   ║
╠══════════════════╬════╤════╬═════╤═════╣
║  umul(int[],int) ║ +0 │ +0 ║  +7 │  +6 ║
║ umul(int[],long) ║ +0 │ +0 ║ +57 │ +56 ║
╚══════════════════╩════╧════╩═════╧═════╝

[runtime performance]
╔═══════════════╦═════════════╦═════════════╦═════════════╗
║               ║ BigInteger  ║   BigInt    ║    int[]    ║
╠═══════════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║     mul(T): 1 ║   +0 │   +0 ║  +24 │  +25 ║  +48 │  +51 ║
║     mul(T): 2 ║   +0 │   +0 ║   +4 │   +5 ║  +14 │  +15 ║
║     mul(T): 4 ║  +41 │  +41 ║   +0 │   +0 ║   +5 │   +5 ║
║     mul(T): 8 ║  +88 │  +88 ║   +0 │   +0 ║   +2 │   +2 ║
║    mul(T): 16 ║ +109 │ +108 ║   +0 │   +0 ║   +4 │   +4 ║
║    mul(T): 32 ║  +84 │  +81 ║   +0 │   +0 ║   +5 │   +5 ║
║    mul(T): 64 ║  +54 │  +54 ║   +0 │   +0 ║   +4 │   +4 ║
║   mul(T): 128 ║  +44 │  +47 ║   +0 │   +0 ║   +3 │   +3 ║
║  mul(int,int) ║   +0 │   +0 ║  +76 │  +81 ║  +93 │  +97 ║
║      mul(int) ║   +0 │   +0 ║ +100 │  +97 ║ +156 │ +159 ║
║ mul(int,long) ║   +0 │   +0 ║ +193 │ +194 ║ +128 │ +128 ║
║     mul(long) ║   +0 │   +0 ║ +124 │ +128 ║ +193 │ +199 ║
╚═══════════════╩══════╧══════╩══════╧══════╩══════╧══════╝

[runtime performance]
╔═══════════════╦════════════╦════════╦═══════╗
║               ║ BigInteger ║ BigInt ║ int[] ║
╠═══════════════╬════════════╬════════╬═══════╣
║   mul(T,T): 1 ║        +29 ║     +0 ║   +16 ║
║   mul(T,T): 2 ║        +31 ║     +0 ║   +10 ║
║   mul(T,T): 4 ║        +47 ║     +0 ║    +8 ║
║   mul(T,T): 8 ║        +82 ║     +0 ║    +4 ║
║  mul(T,T): 16 ║       +107 ║     +0 ║    +0 ║
║  mul(T,T): 32 ║        +95 ║     +0 ║    +1 ║
║  mul(T,T): 64 ║        +99 ║     +0 ║    +0 ║
║ mul(T,T): 128 ║        +71 ║     +0 ║    +2 ║
╚═══════════════╩════════════╩════════╩═══════╝

[heap allocation]
╔══════════════════╦═══════════════╦═══════════════╗
║                  ║      MPN      ║    BigInt     ║
║                  ║     T │ int[] ║     T │ int[] ║
╠══════════════════╬═══════╤═══════╬═══════╤═══════╣
║  umul(int[],int) ║    +0 │    +0 ║    +0 │    +0 ║
║ umul(int[],long) ║    +0 │    +0 ║    +0 │    +0 ║
╚══════════════════╩═══════╧═══════╩═══════╧═══════╝

[heap allocation]
╔═══════════════╦═══════════════╦═════════════════╦═════════════════╗
║               ║  BigInteger   ║     BigInt      ║      int[]      ║
║               ║     T │ int[] ║      T │ int[]  ║      T │ int[]  ║
╠═══════════════╬═══════╤═══════╬════════╤════════╬════════╤════════╣
║     mul(T): 1 ║    +0 │    +0 ║  +2481 │   +563 ║   +932 │   +563 ║
║     mul(T): 2 ║    +0 │    +0 ║  +3040 │   +658 ║  +1156 │   +658 ║
║     mul(T): 4 ║    +0 │    +0 ║  +3636 │   +758 ║  +1394 │   +758 ║
║     mul(T): 8 ║    +0 │    +0 ║  +4104 │   +821 ║  +1581 │   +821 ║
║    mul(T): 16 ║    +0 │    +0 ║  +4681 │   +905 ║  +1812 │   +905 ║
║    mul(T): 32 ║    +0 │    +0 ║  +6209 │  +1278 ║  +2423 │  +1278 ║
║    mul(T): 64 ║    +0 │    +0 ║ +11800 │  +2990 ║  +4660 │  +2990 ║
║   mul(T): 128 ║    +0 │    +0 ║ +43431 │ +13701 ║ +17312 │ +13701 ║
║  mul(int,int) ║    +0 │    +0 ║   +863 │   +155 ║   +211 │   +155 ║
║      mul(int) ║    +0 │    +0 ║   +863 │   +155 ║   +211 │   +155 ║
║ mul(int,long) ║    +0 │    +0 ║  +2127 │   +458 ║   +690 │   +458 ║
║     mul(long) ║    +0 │    +0 ║  +1972 │   +416 ║   +660 │   +416 ║
║   mul(T,T): 1 ║    +0 │    +0 ║  +2481 │   +442 ║   +760 │   +442 ║
║   mul(T,T): 2 ║    +0 │    +0 ║  +3063 │   +521 ║   +954 │   +521 ║
║   mul(T,T): 4 ║    +0 │    +0 ║  +3636 │   +615 ║  +1145 │   +615 ║
║   mul(T,T): 8 ║    +0 │    +0 ║  +4200 │   +678 ║  +1333 │   +678 ║
║  mul(T,T): 16 ║    +0 │    +0 ║  +4754 │   +739 ║  +1518 │   +739 ║
║  mul(T,T): 32 ║    +0 │    +0 ║  +6436 │  +1072 ║  +2078 │  +1072 ║
║  mul(T,T): 64 ║    +0 │    +0 ║ +14145 │  +3036 ║  +4648 │  +3036 ║
╚═══════════════╩═══════╧═══════╩════════╧════════╩════════╧════════╝
```

##### `umul(int[],int)`

This test validates the correctness of `[BigIntMultiplication.umul(int)][BigIntMultiplication]` against `[MPN.mul(...)][MPN]`.

```
[runtime performance]
umul(int[],int)
  289329 in 368ms
╔═════════╦═══════════╦═══════════════╦═════════╦═════════╗
║ length  ║ precision ║     count     ║   MPN   ║ BigInt  ║
╠════╤════╬═════╤═════╬═══════╤═══════╬════╤════╬════╤════╣
║ 36 │ 36 ║ -19 │ -19 ║ 37923 │ 37923 ║  7 │  8 ║  7 │  7 ║
║ 35 │ 35 ║ -15 │ -15 ║ 22700 │ 22700 ║ 14 │  8 ║ 11 │  7 ║
║ 35 │ 35 ║ -11 │ -11 ║ 30346 │ 30346 ║  8 │  7 ║  7 │  8 ║
║ 34 │ 34 ║  -7 │  -7 ║ 22723 │ 22723 ║  6 │  7 ║  7 │  6 ║
║ 34 │ 34 ║  -3 │  -3 ║ 30484 │ 30484 ║  7 │  8 ║  8 │  8 ║
║ 34 │ 34 ║   0 │   0 ║ 15284 │ 15284 ║  8 │  8 ║  8 │  8 ║
║ 34 │ 34 ║   3 │   3 ║ 30346 │ 30346 ║  7 │  7 ║  7 │  6 ║
║ 34 │ 34 ║   7 │   7 ║ 22700 │ 22700 ║  7 │  7 ║  6 │  6 ║
║ 35 │ 35 ║  11 │  11 ║ 30300 │ 30300 ║  8 │ 11 ║  8 │  7 ║
║ 35 │ 35 ║  15 │  15 ║ 22700 │ 22700 ║ 10 │  7 ║  8 │  9 ║
║ 36 │ 36 ║  19 │  19 ║ 22723 │ 22723 ║  7 │  7 ║  6 │  8 ║
║    │    ║     │     ║       │  sum: ║ 89 │ 85 ║ 83 │ 80 ║
║    │    ║     │     ║       │   +%: ║ +0 │ +0 ║ +7 │ +6 ║
╚════╧════╩═════╧═════╩═══════╧═══════╩════╧════╩════╧════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⡽⠋⠉⠉⠙⡙⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⠝⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠴⠋⠀⠀⠀⠀⠀⠈⠄⠑⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡐⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠖⠁⠀⠀⠀⠀⠀⠀⠀⠈⡀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡰⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠈⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⡠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠪⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠈⠉⠑⠒⠒⠒⡲⠮⠔⠤⠤⠤⠤⠔⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠤⠤⠤⠤⠤⠤⢼
⡏⡈⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⢀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⠀⠀⢀⢤⠞⠉⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠠⠀⠀⠑⢄⡀⠀⠀⢀⡠⠊⠀⠀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⡠⠒⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⡀⠀⢀⠤⡪⠕⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠂⠀⠀⠀⠈⠉⠉⠁⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠨⠛⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⢁⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⠀⠀⠀⠀⠀⡠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⣀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⡈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠡⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠡⠀⠀⢀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠑⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
umul(int[],int)
  7749 in 264ms
╔═════════╦═══════════╦════════════╦═══════════════╦═══════════════╗
║         ║           ║            ║      MPN      ║    BigInt     ║
║ length  ║ precision ║   count    ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬═════╤══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 36 ║ -19 │ -19 ║ 873 │  873 ║     0 │     0 ║     0 │     0 ║
║ 35 │ 35 ║ -15 │ -15 ║ 470 │  470 ║     0 │     0 ║     0 │     0 ║
║ 35 │ 35 ║ -11 │ -11 ║ 706 │  706 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║  -7 │  -7 ║ 493 │  493 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║  -3 │  -3 ║ 844 │  844 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║   0 │   0 ║ 464 │  464 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║   3 │   3 ║ 706 │  706 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║   7 │   7 ║ 470 │  470 ║     0 │     0 ║     0 │     0 ║
║ 35 │ 35 ║  11 │  11 ║ 660 │  660 ║     0 │     0 ║     0 │     0 ║
║ 35 │ 35 ║  15 │  15 ║ 470 │  470 ║     0 │     0 ║     0 │     0 ║
║ 36 │ 36 ║  19 │  19 ║ 493 │  493 ║     0 │     0 ║     0 │     0 ║
║    │    ║     │     ║     │ sum: ║     0 │     0 ║     0 │     0 ║
║    │    ║     │     ║     │  +%: ║    +0 │    +0 ║    +0 │    +0 ║
╚════╧════╩═════╧═════╩═════╧══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(T): 1`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

For "small sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations.

For "medium sized" numbers, `BigInteger` outperforms `BigInt` due to the fact that `BigInteger.multiplyToLen(...)` is implemented as an intrinsic, which proves to beat `BigInt`'s critical native implementation of the same algorithm.

For "large sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations. Furthermore, `BigInt` utilizes an implementation of Karatsuba multiplication that is designed to reduce (or even eliminate) the need to instantiate transient `int[]` arrays for calculations. This algorithm is specifically designed to take advantage of any free space available in the `BigInt`'s own magnitude array. The free space in this array is used for calculation, if the space is sufficient. If not sufficient, the algorithm creates necessary arrays. Since this algorithm is implemented in JNI, all transient arrays are freed immediately after use, thus not impacting the heap allocation.

For "very large sized" numbers, `BigInt` outperforms `BigInteger` due to implementat Parallel Karatsuba algorithm. Given input of a size above a threshold, the algorithm breaks the problem into its 3 parts (left, middle, right), and executes 3 threads to perform the calculations in parallel. Due to the recursive nature of the Karatsuba algorithm, subsequent recursion can also result in parallel execution. However, such a situation would only occur for very very very large numbers, because the threshold for recursive parallel execution is doubled for each recursion.

```
[runtime performance]
mul(T): 1
  33393 in 647ms
╔═════════╦═══════════╦═════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision ║    count    ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬═════╤═════╬══════╤══════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 40 │ 40 ║ -64 │ -64 ║ 3303 │ 3303 ║  106 │   72 ║  57 │  61 ║  48 │  55 ║
║ 39 │ 39 ║ -51 │ -51 ║ 2997 │ 2997 ║   57 │   62 ║  50 │  56 ║  44 │  50 ║
║ 38 │ 38 ║ -38 │ -38 ║ 2766 │ 2766 ║   54 │   62 ║  48 │  50 ║  43 │  45 ║
║ 36 │ 36 ║ -25 │ -25 ║ 3022 │ 3022 ║   49 │   59 ║  43 │  47 ║  38 │  37 ║
║ 35 │ 35 ║ -12 │ -12 ║ 3047 │ 3047 ║   49 │   50 ║  49 │  32 ║  35 │  25 ║
║ 34 │ 34 ║   0 │   0 ║ 2610 │ 2610 ║   49 │   51 ║  46 │  30 ║  34 │  23 ║
║ 35 │ 35 ║  12 │  12 ║ 2972 │ 2972 ║   50 │   52 ║  48 │  38 ║  36 │  27 ║
║ 36 │ 36 ║  25 │  25 ║ 3047 │ 3047 ║   50 │   68 ║  47 │  51 ║  43 │  40 ║
║ 38 │ 38 ║  38 │  38 ║ 2741 │ 2741 ║   54 │   72 ║  50 │  57 ║  44 │  46 ║
║ 39 │ 39 ║  51 │  51 ║ 2997 │ 2997 ║   56 │   69 ║  51 │  59 ║  45 │  50 ║
║ 40 │ 40 ║  64 │  64 ║ 2791 │ 2791 ║  111 │   72 ║  63 │  67 ║  50 │  56 ║
║    │    ║     │     ║      │ sum: ║  685 │  689 ║ 552 │ 548 ║ 460 │ 454 ║
║    │    ║     │     ║      │  +%: ║   +0 │   +0 ║ +24 │ +25 ║ +48 │ +51 ║
╚════╧════╩═════╧═════╩══════╧══════╩══════╧══════╩═════╧═════╩═════╧═════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠔⠒⠒⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠉⠉⠉⠉⠑⠒⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠒⠤⠤⠤⠤⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠉⠉⠉⠒⠒⠒⠒⠒⠒⠊⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⡠⠤⠤⠤⠔⠒⠒⠒⠉⠉⠒⠒⠢⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠤⠤⠒⠒⠒⠒⠒⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠒⠢⠤⠤⠤⠤⠤⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠒⠒⠒⠤⠔⠒⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠢⠤⠤⢄⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⠤⠤⠤⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠑⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⣀⠤⠤⠤⠤⠤⠤⣀⣀⣀⣀⣀⠤⠤⠤⠔⠒⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠈⠉⠑⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⢀⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠢⠤⠤⢄⣀⣀⣀⡠⠤⠒⠒⠉⠑⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢄⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T): 1
  17009 in 2559ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 40 │ 40 ║ -64 │ -64 ║ 1639 │ 1639 ║    77 │    45 ║     2 │     5 ║     5 │     5 ║
║ 39 │ 39 ║ -51 │ -51 ║ 1461 │ 1461 ║    73 │    44 ║     2 │     5 ║     5 │     5 ║
║ 38 │ 38 ║ -38 │ -38 ║ 1358 │ 1358 ║    69 │    43 ║     2 │     5 ║     5 │     5 ║
║ 36 │ 36 ║ -25 │ -25 ║ 1486 │ 1486 ║    48 │    31 ║     2 │     5 ║     5 │     5 ║
║ 35 │ 35 ║ -12 │ -12 ║ 1511 │ 1511 ║    16 │    11 ║     2 │     5 ║     5 │     5 ║
║ 34 │ 34 ║   0 │   0 ║ 1330 │ 1330 ║    12 │    10 ║     2 │     5 ║     5 │     5 ║
║ 35 │ 35 ║  12 │  12 ║ 1436 │ 1436 ║    13 │    12 ║     2 │     5 ║     5 │     5 ║
║ 36 │ 36 ║  25 │  25 ║ 1511 │ 1511 ║    51 │    36 ║     2 │     5 ║     5 │     5 ║
║ 38 │ 38 ║  38 │  38 ║ 1333 │ 1333 ║    66 │    43 ║     2 │     5 ║     5 │     5 ║
║ 39 │ 39 ║  51 │  51 ║ 1461 │ 1461 ║    69 │    44 ║     2 │     5 ║     5 │     5 ║
║ 40 │ 40 ║  64 │  64 ║ 1383 │ 1383 ║    74 │    46 ║     2 │     5 ║     5 │     5 ║
║    │    ║     │     ║      │ sum: ║   568 │   365 ║    22 │    55 ║    55 │    55 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +2481 │  +563 ║  +932 │  +563 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(T): 2`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

For "small sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations.

For "medium sized" numbers, `BigInteger` outperforms `BigInt` due to the fact that `BigInteger.multiplyToLen(...)` is implemented as an intrinsic, which proves to beat `BigInt`'s critical native implementation of the same algorithm.

For "large sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations. Furthermore, `BigInt` utilizes an implementation of Karatsuba multiplication that is designed to reduce (or even eliminate) the need to instantiate transient `int[]` arrays for calculations. This algorithm is specifically designed to take advantage of any free space available in the `BigInt`'s own magnitude array. The free space in this array is used for calculation, if the space is sufficient. If not sufficient, the algorithm creates necessary arrays. Since this algorithm is implemented in JNI, all transient arrays are freed immediately after use, thus not impacting the heap allocation.

For "very large sized" numbers, `BigInt` outperforms `BigInteger` due to implementat Parallel Karatsuba algorithm. Given input of a size above a threshold, the algorithm breaks the problem into its 3 parts (left, middle, right), and executes 3 threads to perform the calculations in parallel. Due to the recursive nature of the Karatsuba algorithm, subsequent recursion can also result in parallel execution. However, such a situation would only occur for very very very large numbers, because the threshold for recursive parallel execution is doubled for each recursion.

```
[runtime performance]
mul(T): 2
  131697 in 1971ms
╔═════════╦═════════════╦═══════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║  precision  ║     count     ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬══════╤══════╬═══════╤═══════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 47 │ 47 ║ -128 │ -128 ║ 13287 │ 13287 ║  146 │  113 ║ 114 │ 130 ║ 110 │ 119 ║
║ 44 │ 44 ║ -102 │ -102 ║ 11189 │ 11189 ║   88 │  103 ║  98 │ 107 ║  95 │ 100 ║
║ 41 │ 41 ║  -76 │  -76 ║ 12238 │ 12238 ║   88 │   97 ║  93 │  90 ║  87 │  85 ║
║ 39 │ 39 ║  -51 │  -51 ║ 12238 │ 12238 ║   92 │   83 ║  79 │  72 ║  73 │  67 ║
║ 36 │ 36 ║  -25 │  -25 ║ 11214 │ 11214 ║   54 │   72 ║  63 │  56 ║  54 │  47 ║
║ 34 │ 34 ║    0 │    0 ║ 11339 │ 11339 ║   53 │   61 ║  42 │  44 ║  36 │  32 ║
║ 36 │ 36 ║   25 │   25 ║ 11164 │ 11164 ║   55 │   82 ║  68 │  61 ║  58 │  52 ║
║ 39 │ 39 ║   51 │   51 ║ 12263 │ 12263 ║   96 │   88 ║  89 │  75 ║  74 │  68 ║
║ 41 │ 41 ║   76 │   76 ║ 12213 │ 12213 ║   93 │   97 ║ 100 │  92 ║  90 │  86 ║
║ 44 │ 44 ║  102 │  102 ║ 11189 │ 11189 ║   92 │  107 ║ 105 │ 113 ║ 100 │ 102 ║
║ 47 │ 47 ║  128 │  128 ║ 12263 │ 12263 ║  157 │  121 ║ 124 │ 131 ║ 108 │ 125 ║
║    │    ║      │      ║       │  sum: ║ 1014 │ 1024 ║ 975 │ 971 ║ 885 │ 883 ║
║    │    ║      │      ║       │   +%: ║   +0 │   +0 ║  +4 │  +5 ║ +14 │ +15 ║
╚════╧════╩══════╧══════╩═══════╧═══════╩══════╧══════╩═════╧═════╩═════╧═════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⡩⠝⠛⠉⠙⠫⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠒⠉⠀⠀⠀⢀⡠⠤⠒⠒⠤⡀⠀⠀⠈⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠒⠁⠀⠀⠀⠀⡠⠔⠁⠀⠀⠀⠀⠀⠈⠑⢄⠀⠀⠀⠑⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠊⠁⠀⠀⢀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⡀⠀⠀⠀⠀⠉⠑⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠊⠀⣀⡠⠔⠒⠉⠁⢀⡠⠔⠒⠒⠒⠢⠤⠤⣀⣀⣀⣀⣀⣀⣀⡀⠈⠒⢄⡀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠉⢀⡠⠔⠉⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⢄⠈⠑⠤⣀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠊⠁⢀⡠⠒⠁⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠉⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⣀⠤⠤⣤⠤⠶⣉⣀⣀⣤⣒⣁⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⡀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠈⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⣀⣀⣀⠤⠮⠒⠊⠉⠀⣀⡠⠒⠉⠀⠀⠀⠀⠉⠉⠒⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⠀⠀⠀⠀⢀⣀⠭⠖⠶⣒⠒⠊⠉⠉⠉⠛⠭⢕⣄⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠀⠀⠔⠁⢀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠉⠒⢄⡀⠀⠀⠀⠀⠀⠡⡀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⢹
⣇⣀⢤⠜⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⠤⣀⡀⠀⠐⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠠⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⡒⠒⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡗⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠄⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T): 2
  66161 in 7389ms
╔═════════╦═════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║             ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║  precision  ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤══════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 47 │ 47 ║ -128 │ -128 ║ 6631 │ 6631 ║    83 │    46 ║     2 │     5 ║     5 │     5 ║
║ 44 │ 44 ║ -102 │ -102 ║ 5557 │ 5557 ║    79 │    45 ║     2 │     5 ║     5 │     5 ║
║ 41 │ 41 ║  -76 │  -76 ║ 6094 │ 6094 ║    75 │    44 ║     2 │     5 ║     5 │     5 ║
║ 39 │ 39 ║  -51 │  -51 ║ 6094 │ 6094 ║    68 │    41 ║     2 │     5 ║     5 │     5 ║
║ 36 │ 36 ║  -25 │  -25 ║ 5582 │ 5582 ║    41 │    26 ║     2 │     5 ║     5 │     5 ║
║ 34 │ 34 ║    0 │    0 ║ 5707 │ 5707 ║    13 │    10 ║     2 │     5 ║     5 │     5 ║
║ 36 │ 36 ║   25 │   25 ║ 5532 │ 5532 ║    42 │    29 ║     2 │     5 ║     5 │     5 ║
║ 39 │ 39 ║   51 │   51 ║ 6119 │ 6119 ║    65 │    41 ║     2 │     5 ║     5 │     5 ║
║ 41 │ 41 ║   76 │   76 ║ 6069 │ 6069 ║    71 │    44 ║     2 │     5 ║     5 │     5 ║
║ 44 │ 44 ║  102 │  102 ║ 5557 │ 5557 ║    75 │    45 ║     2 │     5 ║     5 │     5 ║
║ 47 │ 47 ║  128 │  128 ║ 6119 │ 6119 ║    79 │    46 ║     2 │     5 ║     5 │     5 ║
║    │    ║      │      ║      │ sum: ║   691 │   417 ║    22 │    55 ║    55 │    55 ║
║    │    ║      │      ║      │  +%: ║    +0 │    +0 ║ +3040 │  +658 ║ +1156 │  +658 ║
╚════╧════╩══════╧══════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(T): 4`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

For "small sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations.

For "medium sized" numbers, `BigInteger` outperforms `BigInt` due to the fact that `BigInteger.multiplyToLen(...)` is implemented as an intrinsic, which proves to beat `BigInt`'s critical native implementation of the same algorithm.

For "large sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations. Furthermore, `BigInt` utilizes an implementation of Karatsuba multiplication that is designed to reduce (or even eliminate) the need to instantiate transient `int[]` arrays for calculations. This algorithm is specifically designed to take advantage of any free space available in the `BigInt`'s own magnitude array. The free space in this array is used for calculation, if the space is sufficient. If not sufficient, the algorithm creates necessary arrays. Since this algorithm is implemented in JNI, all transient arrays are freed immediately after use, thus not impacting the heap allocation.

For "very large sized" numbers, `BigInt` outperforms `BigInteger` due to implementat Parallel Karatsuba algorithm. Given input of a size above a threshold, the algorithm breaks the problem into its 3 parts (left, middle, right), and executes 3 threads to perform the calculations in parallel. Due to the recursive nature of the Karatsuba algorithm, subsequent recursion can also result in parallel execution. However, such a situation would only occur for very very very large numbers, because the threshold for recursive parallel execution is doubled for each recursion.

```
[runtime performance]
mul(T): 4
  231417 in 6125ms
╔═════════╦═════════════╦═══════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║  precision  ║     count     ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤══════╬═══════╤═══════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 60 │ 60 ║ -256 │ -256 ║ 21899 │ 21611 ║  218 │  210 ║  331 │  357 ║  324 │  342 ║
║ 55 │ 55 ║ -204 │ -204 ║ 22025 │ 21617 ║  194 │  185 ║  283 │  299 ║  273 │  292 ║
║ 49 │ 49 ║ -153 │ -153 ║ 21206 │ 21582 ║  167 │  159 ║  226 │  236 ║  217 │  223 ║
║ 44 │ 44 ║ -102 │ -102 ║ 19018 │ 19742 ║  129 │  127 ║  178 │  173 ║  174 │  165 ║
║ 39 │ 39 ║  -51 │  -51 ║ 22662 │ 21618 ║  105 │  104 ║  135 │  119 ║  125 │  105 ║
║ 34 │ 34 ║    0 │    0 ║ 18815 │ 19803 ║  100 │   87 ║   92 │   85 ║   81 │   63 ║
║ 39 │ 39 ║   51 │   51 ║ 20544 │ 19792 ║  103 │  122 ║  145 │  124 ║  132 │  115 ║
║ 44 │ 44 ║  102 │  102 ║ 21803 │ 21607 ║  136 │  146 ║  198 │  178 ║  179 │  167 ║
║ 49 │ 49 ║  153 │  153 ║ 18421 │ 19717 ║  172 │  175 ║  236 │  238 ║  224 │  225 ║
║ 55 │ 55 ║  204 │  204 ║ 22025 │ 21617 ║  193 │  201 ║  287 │  298 ║  273 │  288 ║
║ 60 │ 60 ║  256 │  256 ║ 21899 │ 21611 ║  222 │  224 ║  344 │  353 ║  329 │  343 ║
║    │    ║      │      ║       │  sum: ║ 1739 │ 1740 ║ 2455 │ 2460 ║ 2331 │ 2328 ║
║    │    ║      │      ║       │   +%: ║  +41 │  +41 ║   +0 │   +0 ║   +5 │   +5 ║
╚════╧════╩══════╧══════╩═══════╧═══════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⡩⠭⠭⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠒⠒⠒⢒⠖⠥⠤⣠⣔⣒⣒⣤⣀⣀⣐⣄⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠔⠒⠉⠁⠀⠀⠀⠀⡠⠒⠁⢀⠔⠉⠀⠀⠀⠀⠀⠀⠑⢄⡀⠑⢄⠀⠈⠉⠑⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠑⢄⡀⠀⠀⠀⠀⠀⠉⠑⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⢀⣀⣀⠤⠤⠤⠒⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠌⢀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⢄⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠊⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⡡⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⠈⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠑⠒⠒⠢⠤⠤⢄⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⡡⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠉⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⢊⠕⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⡀⠀⠑⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⡡⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣀⠀⠈⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠊⢁⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠀⠑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠈⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⢁⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⡀⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⡡⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⠈⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⡠⠔⢉⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠉⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⢀⠤⠒⢉⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠉⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠊⣁⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠉⠢⠤⠤⠤⠤⠤⠤⠤⢼
⡗⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T): 4
  116229 in 15672ms
╔═════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║             ║               ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║  precision  ║     count     ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 60 │ 60 ║ -256 │ -256 ║ 10553 │ 10683 ║    92 │    49 ║     2 │     5 ║     5 │     5 ║
║ 55 │ 55 ║ -204 │ -204 ║ 10941 │ 10709 ║    87 │    47 ║     2 │     5 ║     5 │     5 ║
║ 49 │ 49 ║ -153 │ -153 ║  9520 │ 10814 ║    89 │    50 ║     2 │     5 ║     5 │     5 ║
║ 44 │ 44 ║ -102 │ -102 ║ 10142 │  9934 ║    77 │    44 ║     2 │     5 ║     5 │     5 ║
║ 39 │ 39 ║  -51 │  -51 ║ 11226 │ 10822 ║    65 │    39 ║     2 │     5 ║     5 │     5 ║
║ 34 │ 34 ║    0 │    0 ║ 11787 │ 10189 ║    17 │    13 ║     2 │     5 ║     5 │     5 ║
║ 39 │ 39 ║   51 │   51 ║  9804 │  9838 ║    64 │    40 ║     2 │     5 ║     5 │     5 ║
║ 44 │ 44 ║  102 │  102 ║ 11207 │ 10893 ║    73 │    43 ║     2 │     5 ║     5 │     5 ║
║ 49 │ 49 ║  153 │  153 ║  8455 │  9855 ║    87 │    51 ║     2 │     5 ║     5 │     5 ║
║ 55 │ 55 ║  204 │  204 ║ 10941 │ 10709 ║    83 │    47 ║     2 │     5 ║     5 │     5 ║
║ 60 │ 60 ║  256 │  256 ║ 10553 │ 10683 ║    88 │    49 ║     2 │     5 ║     5 │     5 ║
║    │    ║      │      ║       │  sum: ║   822 │   472 ║    22 │    55 ║    55 │    55 ║
║    │    ║      │      ║       │   +%: ║    +0 │    +0 ║ +3636 │  +758 ║ +1394 │  +758 ║
╚════╧════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(T): 8`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

For "small sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations.

For "medium sized" numbers, `BigInteger` outperforms `BigInt` due to the fact that `BigInteger.multiplyToLen(...)` is implemented as an intrinsic, which proves to beat `BigInt`'s critical native implementation of the same algorithm.

For "large sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations. Furthermore, `BigInt` utilizes an implementation of Karatsuba multiplication that is designed to reduce (or even eliminate) the need to instantiate transient `int[]` arrays for calculations. This algorithm is specifically designed to take advantage of any free space available in the `BigInt`'s own magnitude array. The free space in this array is used for calculation, if the space is sufficient. If not sufficient, the algorithm creates necessary arrays. Since this algorithm is implemented in JNI, all transient arrays are freed immediately after use, thus not impacting the heap allocation.

For "very large sized" numbers, `BigInt` outperforms `BigInteger` due to implementat Parallel Karatsuba algorithm. Given input of a size above a threshold, the algorithm breaks the problem into its 3 parts (left, middle, right), and executes 3 threads to perform the calculations in parallel. Due to the recursive nature of the Karatsuba algorithm, subsequent recursion can also result in parallel execution. However, such a situation would only occur for very very very large numbers, because the threshold for recursive parallel execution is doubled for each recursion.

```
[runtime performance]
mul(T): 8
  336513 in 19334ms
╔═════════╦═════════════╦═══════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║  precision  ║     count     ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬══════╤══════╬═══════╤═══════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 87 │ 87 ║ -512 │ -512 ║ 32131 │ 31235 ║  582 │  550 ║ 1162 │ 1168 ║ 1145 │ 1147 ║
║ 76 │ 76 ║ -409 │ -409 ║ 34421 │ 31429 ║  477 │  462 ║  930 │  946 ║  913 │  929 ║
║ 65 │ 65 ║ -307 │ -307 ║ 28834 │ 31486 ║  384 │  367 ║  730 │  728 ║  711 │  707 ║
║ 55 │ 55 ║ -204 │ -204 ║ 26282 │ 28862 ║  277 │  278 ║  493 │  525 ║  476 │  510 ║
║ 44 │ 44 ║ -102 │ -102 ║ 30310 │ 31202 ║  189 │  200 ║  312 │  298 ║  296 │  285 ║
║ 34 │ 34 ║    0 │    0 ║ 31507 │ 27035 ║   88 │  130 ║  143 │  141 ║  132 │  116 ║
║ 44 │ 44 ║  102 │  102 ║ 30260 │ 31152 ║  195 │  220 ║  316 │  305 ║  298 │  284 ║
║ 55 │ 55 ║  204 │  204 ║ 28779 │ 31435 ║  290 │  308 ║  513 │  533 ║  492 │  514 ║
║ 65 │ 65 ║  307 │  307 ║ 26337 │ 28913 ║  396 │  394 ║  744 │  739 ║  734 │  713 ║
║ 76 │ 76 ║  409 │  409 ║ 34421 │ 31429 ║  486 │  489 ║  941 │  950 ║  914 │  933 ║
║ 87 │ 87 ║  512 │  512 ║ 32131 │ 31235 ║  588 │  576 ║ 1167 │ 1167 ║ 1138 │ 1150 ║
║    │    ║      │      ║       │  sum: ║ 3952 │ 3974 ║ 7451 │ 7500 ║ 7249 │ 7288 ║
║    │    ║      │      ║       │   +%: ║  +88 │  +88 ║   +0 │   +0 ║   +2 │   +2 ║
╚════╧════╩══════╧══════╩═══════╧═══════╩══════╧══════╩══════╧══════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⠭⠭⢛⣛⠭⠭⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠔⠒⠉⠁⢀⠔⡪⠥⠤⠭⡒⢄⠈⠑⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠔⠒⠒⠉⠉⠀⠀⠀⠀⠀⢀⢔⠕⠉⠀⠀⠀⠀⠈⠢⡑⢄⠀⠀⠀⠈⠉⠑⠒⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⡠⠤⠔⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⡒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠤⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⣀⣀⣀⠤⠤⠔⠒⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⡪⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠪⡢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠒⠤⠤⢄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⡪⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠪⡢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⡪⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠪⡢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⡪⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠪⡢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡪⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠪⡢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⢔⠝⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠪⡢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⢔⠕⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠪⡢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⡪⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢕⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⢔⠕⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠪⡢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⢔⠮⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠵⡢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⣀⢔⠮⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠭⡢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⣀⢔⠮⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠭⡒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⣀⢔⠮⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠪⣒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣗⠞⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠪⢕⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T): 8
  173093 in 31403ms
╔═════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║             ║               ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║  precision  ║     count     ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 87 │ 87 ║ -512 │ -512 ║ 15601 │ 16065 ║   102 │    52 ║     2 │     5 ║     5 │     5 ║
║ 76 │ 76 ║ -409 │ -409 ║ 16377 │ 16161 ║    96 │    50 ║     2 │     5 ║     5 │     5 ║
║ 65 │ 65 ║ -307 │ -307 ║ 15956 │ 16214 ║    93 │    50 ║     2 │     5 ║     5 │     5 ║
║ 55 │ 55 ║ -204 │ -204 ║ 15510 │ 14618 ║    84 │    45 ║     2 │     5 ║     5 │     5 ║
║ 44 │ 44 ║ -102 │ -102 ║ 15506 │ 15934 ║    77 │    43 ║     2 │     5 ║     5 │     5 ║
║ 34 │ 34 ║    0 │    0 ║ 14143 │ 14059 ║    40 │    26 ║     2 │     5 ║     5 │     5 ║
║ 44 │ 44 ║  102 │  102 ║ 15456 │ 15884 ║    73 │    43 ║     2 │     5 ║     5 │     5 ║
║ 55 │ 55 ║  204 │  204 ║ 17199 │ 16047 ║    80 │    45 ║     2 │     5 ║     5 │     5 ║
║ 65 │ 65 ║  307 │  307 ║ 14267 │ 14785 ║    90 │    51 ║     2 │     5 ║     5 │     5 ║
║ 76 │ 76 ║  409 │  409 ║ 16377 │ 16161 ║    92 │    50 ║     2 │     5 ║     5 │     5 ║
║ 87 │ 87 ║  512 │  512 ║ 15601 │ 16065 ║    98 │    52 ║     2 │     5 ║     5 │     5 ║
║    │    ║      │      ║       │  sum: ║   925 │   507 ║    22 │    55 ║    55 │    55 ║
║    │    ║      │      ║       │   +%: ║    +0 │    +0 ║ +4104 │  +821 ║ +1581 │  +821 ║
╚════╧════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(T): 16`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

For "small sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations.

For "medium sized" numbers, `BigInteger` outperforms `BigInt` due to the fact that `BigInteger.multiplyToLen(...)` is implemented as an intrinsic, which proves to beat `BigInt`'s critical native implementation of the same algorithm.

For "large sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations. Furthermore, `BigInt` utilizes an implementation of Karatsuba multiplication that is designed to reduce (or even eliminate) the need to instantiate transient `int[]` arrays for calculations. This algorithm is specifically designed to take advantage of any free space available in the `BigInt`'s own magnitude array. The free space in this array is used for calculation, if the space is sufficient. If not sufficient, the algorithm creates necessary arrays. Since this algorithm is implemented in JNI, all transient arrays are freed immediately after use, thus not impacting the heap allocation.

For "very large sized" numbers, `BigInt` outperforms `BigInteger` due to implementat Parallel Karatsuba algorithm. Given input of a size above a threshold, the algorithm breaks the problem into its 3 parts (left, middle, right), and executes 3 threads to perform the calculations in parallel. Due to the recursive nature of the Karatsuba algorithm, subsequent recursion can also result in parallel execution. However, such a situation would only occur for very very very large numbers, because the threshold for recursive parallel execution is doubled for each recursion.

```
[runtime performance]
mul(T): 16
  264681 in 40285ms
╔═══════════╦═══════════════╦═══════════════╦═══════════════╦═══════════════╦═══════════════╗
║  length   ║   precision   ║     count     ║  BigInteger   ║    BigInt     ║     int[]     ║
╠═════╤═════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 140 │ 140 ║ -1024 │ -1024 ║ 23875 │ 24835 ║  2242 │  2152 ║  4336 │  4266 ║  4180 │  4052 ║
║ 119 │ 119 ║  -819 │  -819 ║ 24437 │ 24165 ║  1777 │  1667 ║  3681 │  3463 ║  3514 │  3290 ║
║  97 │  97 ║  -614 │  -614 ║ 19313 │ 22785 ║  1241 │  1254 ║  2909 │  2859 ║  2815 │  2797 ║
║  76 │  76 ║  -409 │  -409 ║ 29111 │ 24619 ║   876 │   861 ║  1860 │  1922 ║  1833 │  1901 ║
║  55 │  55 ║  -204 │  -204 ║ 23774 │ 24778 ║   490 │   504 ║   971 │  1013 ║   973 │   994 ║
║  34 │  34 ║     0 │     0 ║ 22611 │ 21267 ║   216 │   319 ║   389 │   369 ║   380 │   338 ║
║  55 │  55 ║   204 │   204 ║ 23724 │ 24728 ║   493 │   536 ║   973 │  1015 ║   967 │  1006 ║
║  76 │  76 ║   409 │   409 ║ 29111 │ 24619 ║   878 │   886 ║  1854 │  1920 ║  1836 │  1895 ║
║  97 │  97 ║   614 │   614 ║ 19313 │ 22785 ║  1255 │  1250 ║  2989 │  3038 ║  2816 │  2813 ║
║ 119 │ 119 ║   819 │   819 ║ 24437 │ 24165 ║  1765 │  1733 ║  3869 │  3591 ║  3517 │  3331 ║
║ 140 │ 140 ║  1024 │  1024 ║ 23875 │ 24835 ║  2292 │  2190 ║  4437 │  4388 ║  4136 │  4127 ║
║     │     ║       │       ║       │  sum: ║ 13525 │ 13352 ║ 28268 │ 27844 ║ 26967 │ 26544 ║
║     │     ║       │       ║       │   +%: ║  +109 │  +108 ║    +0 │    +0 ║    +4 │    +4 ║
╚═════╧═════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⡩⠭⠛⠛⠛⢉⡉⠙⠛⠛⠭⢍⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠒⠊⠉⠁⠀⠀⠀⠀⡠⠚⠉⠉⠛⠦⡀⠀⠀⠀⠈⠉⠒⠒⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠔⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠈⢢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠒⠢⠤⠤⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠞⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⣀⡠⠤⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠱⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠤⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠳⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡰⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠞⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⢦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠞⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⡢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⢤⠞⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠪⡑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⣔⠕⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢄⠉⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⣒⠭⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⡀⠈⠉⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⣀⠤⠒⠊⣁⠤⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⣀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⢀⡠⠒⠉⢀⡠⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠊⢁⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠑⠒⠒⠒⠒⠒⠒⠒⢺
⡗⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⢄⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T): 16
  146377 in 43992ms
╔═══════════╦═══════════════╦═══════════════╦═══════════════╦═══════════════╦═══════════════╗
║           ║               ║               ║  BigInteger   ║    BigInt     ║     int[]     ║
║  length   ║   precision   ║     count     ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠═════╤═════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 140 │ 140 ║ -1024 │ -1024 ║ 14699 │ 13607 ║   111 │    55 ║     2 │     5 ║     5 │     5 ║
║ 119 │ 119 ║  -819 │  -819 ║ 13057 │ 13703 ║   111 │    56 ║     2 │     5 ║     5 │     5 ║
║  97 │  97 ║  -614 │  -614 ║ 12889 │ 12371 ║   100 │    52 ║     2 │     5 ║     5 │     5 ║
║  76 │  76 ║  -409 │  -409 ║ 12205 │ 13525 ║   102 │    52 ║     2 │     5 ║     5 │     5 ║
║  55 │  55 ║  -204 │  -204 ║ 13806 │ 13632 ║    85 │    45 ║     2 │     5 ║     5 │     5 ║
║  34 │  34 ║     0 │     0 ║ 12015 │ 11651 ║    56 │    33 ║     2 │     5 ║     5 │     5 ║
║  55 │  55 ║   204 │   204 ║ 13756 │ 13582 ║    81 │    45 ║     2 │     5 ║     5 │     5 ║
║  76 │  76 ║   409 │   409 ║ 12205 │ 13525 ║    97 │    52 ║     2 │     5 ║     5 │     5 ║
║  97 │  97 ║   614 │   614 ║ 12889 │ 12371 ║    96 │    52 ║     2 │     5 ║     5 │     5 ║
║ 119 │ 119 ║   819 │   819 ║ 13057 │ 13703 ║   106 │    56 ║     2 │     5 ║     5 │     5 ║
║ 140 │ 140 ║  1024 │  1024 ║ 14699 │ 13607 ║   107 │    55 ║     2 │     5 ║     5 │     5 ║
║     │     ║       │       ║       │  sum: ║  1052 │   553 ║    22 │    55 ║    55 │    55 ║
║     │     ║       │       ║       │   +%: ║    +0 │    +0 ║ +4681 │  +905 ║ +1812 │  +905 ║
╚═════╧═════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(T): 32`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

For "small sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations.

For "medium sized" numbers, `BigInteger` outperforms `BigInt` due to the fact that `BigInteger.multiplyToLen(...)` is implemented as an intrinsic, which proves to beat `BigInt`'s critical native implementation of the same algorithm.

For "large sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations. Furthermore, `BigInt` utilizes an implementation of Karatsuba multiplication that is designed to reduce (or even eliminate) the need to instantiate transient `int[]` arrays for calculations. This algorithm is specifically designed to take advantage of any free space available in the `BigInt`'s own magnitude array. The free space in this array is used for calculation, if the space is sufficient. If not sufficient, the algorithm creates necessary arrays. Since this algorithm is implemented in JNI, all transient arrays are freed immediately after use, thus not impacting the heap allocation.

For "very large sized" numbers, `BigInt` outperforms `BigInteger` due to implementat Parallel Karatsuba algorithm. Given input of a size above a threshold, the algorithm breaks the problem into its 3 parts (left, middle, right), and executes 3 threads to perform the calculations in parallel. Due to the recursive nature of the Karatsuba algorithm, subsequent recursion can also result in parallel execution. However, such a situation would only occur for very very very large numbers, because the threshold for recursive parallel execution is doubled for each recursion.

```
[runtime performance]
mul(T): 32
  137393 in 58669ms
╔═══════════╦═══════════════╦═══════════════╦═══════════════╦═══════════════╦═══════════════╗
║  length   ║   precision   ║     count     ║  BigInteger   ║    BigInt     ║     int[]     ║
╠═════╤═════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 246 │ 246 ║ -2048 │ -2048 ║ 14851 │ 12815 ║  9146 │  8881 ║ 15484 │ 14274 ║ 14785 │ 13656 ║
║ 204 │ 204 ║ -1638 │ -1638 ║ 12157 │ 12625 ║  6916 │  6982 ║ 11440 │ 11558 ║ 10869 │ 11011 ║
║ 161 │ 161 ║ -1228 │ -1228 ║ 10145 │ 11429 ║  5662 │  5499 ║  9834 │  9766 ║  9355 │  9348 ║
║ 119 │ 119 ║  -819 │  -819 ║  9259 │ 13159 ║  3309 │  3670 ║  8297 │  8575 ║  7945 │  8252 ║
║  76 │  76 ║  -409 │  -409 ║ 15938 │ 12522 ║  1489 │  1572 ║  3383 │  3684 ║  3351 │  3631 ║
║  34 │  34 ║     0 │     0 ║ 11643 │ 11243 ║   473 │   621 ║   934 │  1024 ║   905 │   979 ║
║  76 │  76 ║   409 │   409 ║ 15888 │ 12472 ║  1501 │  1608 ║  3391 │  3726 ║  3359 │  3840 ║
║ 119 │ 119 ║   819 │   819 ║  9259 │ 13159 ║  3340 │  3673 ║  8542 │  8869 ║  7977 │  8272 ║
║ 161 │ 161 ║  1228 │  1228 ║ 10145 │ 11429 ║  5883 │  5641 ║ 10067 │ 10013 ║  9371 │  9376 ║
║ 204 │ 204 ║  1638 │  1638 ║ 12157 │ 12625 ║  6697 │  7100 ║ 11670 │ 11765 ║ 10900 │ 11069 ║
║ 246 │ 246 ║  2048 │  2048 ║ 14851 │ 12815 ║  9258 │  8636 ║ 15725 │ 14594 ║ 14905 │ 13718 ║
║     │     ║       │       ║       │  sum: ║ 53674 │ 53883 ║ 98767 │ 97848 ║ 93722 │ 93152 ║
║     │     ║       │       ║       │   +%: ║   +84 │   +81 ║    +0 │    +0 ║    +5 │    +5 ║
╚═════╧═════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⠭⠝⠛⠋⠉⠉⡉⠉⠉⠛⠫⠭⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠒⠉⠁⠀⠀⠀⠀⡠⠚⠉⠉⠑⠦⡀⠀⠀⠀⠈⠉⠒⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠘⢄⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠳⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠔⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠣⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠢⠤⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⣀⡠⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠎⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡰⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠥⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡬⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢔⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠕⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠤⠤⠤⠤⠒⢊⠥⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠉⠑⠒⠒⠒⠒⠒⠢⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠔⠊⢉⣁⠤⠤⠒⠒⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠒⠤⠤⠤⠤⠤⢄⣀⠀⠀⠉⠒⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⡠⠒⠉⣀⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠤⢄⡀⠈⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⣀⠔⠉⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⡀⠈⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⡠⠊⢀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢄⡀⠉⠢⣀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T): 32
  63441 in 41622ms
╔═══════════╦═══════════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║           ║               ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║  length   ║   precision   ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠═════╤═════╬═══════╤═══════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 246 │ 246 ║ -2048 │ -2048 ║ 8495 │ 5835 ║   141 │    79 ║     2 │     5 ║     5 │     5 ║
║ 204 │ 204 ║ -1638 │ -1638 ║ 3831 │ 5803 ║   175 │    94 ║     2 │     5 ║     5 │     5 ║
║ 161 │ 161 ║ -1228 │ -1228 ║ 6183 │ 5229 ║   125 │    68 ║     2 │     5 ║     5 │     5 ║
║ 119 │ 119 ║  -819 │  -819 ║ 5621 │ 5927 ║   120 │    62 ║     2 │     5 ║     5 │     5 ║
║  76 │  76 ║  -409 │  -409 ║ 4888 │ 5652 ║   104 │    53 ║     2 │     5 ║     5 │     5 ║
║  34 │  34 ║     0 │     0 ║ 4355 │ 5499 ║    80 │    46 ║     2 │     5 ║     5 │     5 ║
║  76 │  76 ║   409 │   409 ║ 4838 │ 5602 ║    99 │    53 ║     2 │     5 ║     5 │     5 ║
║ 119 │ 119 ║   819 │   819 ║ 5621 │ 5927 ║   115 │    62 ║     2 │     5 ║     5 │     5 ║
║ 161 │ 161 ║  1228 │  1228 ║ 6183 │ 5229 ║   121 │    68 ║     2 │     5 ║     5 │     5 ║
║ 204 │ 204 ║  1638 │  1638 ║ 3831 │ 5803 ║   170 │    94 ║     2 │     5 ║     5 │     5 ║
║ 246 │ 246 ║  2048 │  2048 ║ 8495 │ 5835 ║   138 │    79 ║     2 │     5 ║     5 │     5 ║
║     │     ║       │       ║      │ sum: ║  1388 │   758 ║    22 │    55 ║    55 │    55 ║
║     │     ║       │       ║      │  +%: ║    +0 │    +0 ║ +6209 │ +1278 ║ +2423 │ +1278 ║
╚═════╧═════╩═══════╧═══════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(T): 64`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

For "small sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations.

For "medium sized" numbers, `BigInteger` outperforms `BigInt` due to the fact that `BigInteger.multiplyToLen(...)` is implemented as an intrinsic, which proves to beat `BigInt`'s critical native implementation of the same algorithm.

For "large sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations. Furthermore, `BigInt` utilizes an implementation of Karatsuba multiplication that is designed to reduce (or even eliminate) the need to instantiate transient `int[]` arrays for calculations. This algorithm is specifically designed to take advantage of any free space available in the `BigInt`'s own magnitude array. The free space in this array is used for calculation, if the space is sufficient. If not sufficient, the algorithm creates necessary arrays. Since this algorithm is implemented in JNI, all transient arrays are freed immediately after use, thus not impacting the heap allocation.

For "very large sized" numbers, `BigInt` outperforms `BigInteger` due to implementat Parallel Karatsuba algorithm. Given input of a size above a threshold, the algorithm breaks the problem into its 3 parts (left, middle, right), and executes 3 threads to perform the calculations in parallel. Due to the recursive nature of the Karatsuba algorithm, subsequent recursion can also result in parallel execution. However, such a situation would only occur for very very very large numbers, because the threshold for recursive parallel execution is doubled for each recursion.

```
[runtime performance]
mul(T): 64
  63673 in 85017ms
╔═══════════╦═══════════════╦═════════════╦═════════════════╦═════════════════╦═════════════════╗
║  length   ║   precision   ║    count    ║   BigInteger    ║     BigInt      ║      int[]      ║
╠═════╤═════╬═══════╤═══════╬══════╤══════╬════════╤════════╬════════╤════════╬════════╤════════╣
║ 459 │ 459 ║ -4096 │ -4096 ║ 6727 │ 5695 ║  32919 │  33061 ║  49065 │  50393 ║  47273 │  48792 ║
║ 374 │ 374 ║ -3276 │ -3276 ║ 4613 │ 5757 ║  25661 │  28098 ║  37963 │  39695 ║  36538 │  38029 ║
║ 289 │ 289 ║ -2457 │ -2457 ║ 6741 │ 5609 ║  21506 │  21822 ║  31845 │  31599 ║  30637 │  30297 ║
║ 204 │ 204 ║ -1638 │ -1638 ║ 4267 │ 5615 ║  17895 │  17456 ║  27172 │  26247 ║  26132 │  25330 ║
║ 119 │ 119 ║  -819 │  -819 ║ 5638 │ 5850 ║  11844 │   9966 ║  22182 │  21843 ║  21450 │  21328 ║
║  34 │  34 ║     0 │     0 ║ 6651 │ 5571 ║   1649 │   1799 ║   3670 │   3779 ║   3635 │   3675 ║
║ 119 │ 119 ║   819 │   819 ║ 5588 │ 5800 ║  11566 │  10112 ║  22282 │  22501 ║  21661 │  21488 ║
║ 204 │ 204 ║  1638 │  1638 ║ 4267 │ 5615 ║  17759 │  17135 ║  27391 │  26691 ║  26000 │  25274 ║
║ 289 │ 289 ║  2457 │  2457 ║ 6741 │ 5609 ║  21350 │  22095 ║  32192 │  31847 ║  30536 │  30584 ║
║ 374 │ 374 ║  3276 │  3276 ║ 4613 │ 5757 ║  26126 │  28490 ║  38346 │  39955 ║  36722 │  38137 ║
║ 459 │ 459 ║  4096 │  4096 ║ 6727 │ 5695 ║  32235 │  33237 ║  48974 │  50911 ║  47252 │  48947 ║
║     │     ║       │       ║      │ sum: ║ 220510 │ 223271 ║ 341082 │ 345461 ║ 327836 │ 331881 ║
║     │     ║       │       ║      │  +%: ║    +54 │    +54 ║     +0 │     +0 ║     +4 │     +4 ║
╚═════╧═════╩═══════╧═══════╩══════╧══════╩════════╧════════╩════════╧════════╩════════╧════════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⠝⠋⠉⠉⠉⠛⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠊⠁⠀⢀⠔⠲⡄⠀⠀⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠊⠀⠀⠀⢠⠂⠀⠀⠈⢆⠀⠀⠀⠈⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠀⠀⠀⠀⠀⢀⠃⠀⠀⠀⠀⠈⠆⠀⠀⠀⠀⠀⠈⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠆⠀⠀⠀⠀⠀⠀⠘⡄⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡜⠀⠀⠀⠀⠀⠀⠀⠀⠰⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡰⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢢⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⢀⣀⠤⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢅⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣐⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠦⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⡠⢤⠮⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢕⡢⢄⣀⣀⣀⡠⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⣊⡩⠥⠒⠒⠒⠒⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠢⠤⠤⠤⠤⠤⣈⡉⠒⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⢊⡡⠔⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⣀⡉⠒⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⢉⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⡈⠑⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⣀⠔⠊⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⠉⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⢀⠤⠊⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⢄⠈⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⡠⠔⢁⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⣀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠊⡠⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠉⠢⠤⠤⠤⠤⠤⠤⠤⢼
⡗⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T): 64
  30301 in 52866ms
╔═══════════╦═══════════════╦═════════════╦═══════════════╦════════════════╦═══════════════╗
║           ║               ║             ║  BigInteger   ║     BigInt     ║     int[]     ║
║  length   ║   precision   ║    count    ║     T │ int[] ║     T │ int[]  ║     T │ int[] ║
╠═════╤═════╬═══════╤═══════╬══════╤══════╬═══════╤═══════╬════════╤═══════╬═══════╤═══════╣
║ 459 │ 459 ║ -4096 │ -4096 ║ 3475 │ 2619 ║   341 │   238 ║      2 │     5 ║     5 │     5 ║
║ 374 │ 374 ║ -3276 │ -3276 ║ 1677 │ 2723 ║   339 │   220 ║      2 │     5 ║     5 │     5 ║
║ 289 │ 289 ║ -2457 │ -2457 ║ 2967 │ 2463 ║   223 │   143 ║      2 │     5 ║     5 │     5 ║
║ 204 │ 204 ║ -1638 │ -1638 ║ 2085 │ 2753 ║   224 │   137 ║      2 │     5 ║     5 │     5 ║
║ 119 │ 119 ║  -819 │  -819 ║ 2946 │ 2704 ║   149 │    87 ║      2 │     5 ║     5 │     5 ║
║  34 │  34 ║     0 │     0 ║ 2951 │ 2727 ║    88 │    49 ║      2 │     5 ║     5 │     5 ║
║ 119 │ 119 ║   819 │   819 ║ 2896 │ 2654 ║   145 │    88 ║      2 │     5 ║     5 │     5 ║
║ 204 │ 204 ║  1638 │  1638 ║ 2085 │ 2753 ║   219 │   137 ║      2 │     5 ║     5 │     5 ║
║ 289 │ 289 ║  2457 │  2457 ║ 2967 │ 2463 ║   219 │   143 ║      2 │     5 ║     5 │     5 ║
║ 374 │ 374 ║  3276 │  3276 ║ 1677 │ 2723 ║   334 │   220 ║      2 │     5 ║     5 │     5 ║
║ 459 │ 459 ║  4096 │  4096 ║ 3475 │ 2619 ║   337 │   238 ║      2 │     5 ║     5 │     5 ║
║     │     ║       │       ║      │ sum: ║  2618 │  1700 ║     22 │    55 ║    55 │    55 ║
║     │     ║       │       ║      │  +%: ║    +0 │    +0 ║ +11800 │ +2990 ║ +4660 │ +2990 ║
╚═════╧═════╩═══════╧═══════╩══════╧══════╩═══════╧═══════╩════════╧═══════╩═══════╧═══════╝
```

##### `mul(T): 128`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

For "small sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations.

For "medium sized" numbers, `BigInteger` outperforms `BigInt` due to the fact that `BigInteger.multiplyToLen(...)` is implemented as an intrinsic, which proves to beat `BigInt`'s critical native implementation of the same algorithm.

For "large sized" numbers, `BigInt` outperforms `BigInteger` due to the efficiency gained from mutable design, and the reuse of the underlying magnitude array for calculations. Furthermore, `BigInt` utilizes an implementation of Karatsuba multiplication that is designed to reduce (or even eliminate) the need to instantiate transient `int[]` arrays for calculations. This algorithm is specifically designed to take advantage of any free space available in the `BigInt`'s own magnitude array. The free space in this array is used for calculation, if the space is sufficient. If not sufficient, the algorithm creates necessary arrays. Since this algorithm is implemented in JNI, all transient arrays are freed immediately after use, thus not impacting the heap allocation.

For "very large sized" numbers, `BigInt` outperforms `BigInteger` due to implementat Parallel Karatsuba algorithm. Given input of a size above a threshold, the algorithm breaks the problem into its 3 parts (left, middle, right), and executes 3 threads to perform the calculations in parallel. Due to the recursive nature of the Karatsuba algorithm, subsequent recursion can also result in parallel execution. However, such a situation would only occur for very very very large numbers, because the threshold for recursive parallel execution is doubled for each recursion.

```
[runtime performance]
mul(T): 128
  28737 in 117634ms
╔═══════════╦═══════════════╦═════════════╦═════════════════╦═══════════════════╦═══════════════════╗
║  length   ║   precision   ║    count    ║   BigInteger    ║      BigInt       ║       int[]       ║
╠═════╤═════╬═══════╤═══════╬══════╤══════╬════════╤════════╬═════════╤═════════╬═════════╤═════════╣
║ 884 │ 884 ║ -8192 │ -8192 ║ 2015 │ 2483 ║ 117724 │ 112059 ║  146429 │  142027 ║  140097 │  142095 ║
║ 714 │ 714 ║ -6553 │ -6553 ║ 2609 │ 2521 ║  83179 │  81935 ║  122844 │  125426 ║  118188 │  120845 ║
║ 544 │ 544 ║ -4915 │ -4915 ║ 1517 │ 2409 ║  77596 │  68110 ║  109916 │  102789 ║  109101 │  100663 ║
║ 374 │ 374 ║ -3276 │ -3276 ║ 3379 │ 2607 ║  58804 │  51665 ║   83965 │   75660 ║   81704 │   72452 ║
║ 204 │ 204 ║ -1638 │ -1638 ║ 2114 │ 2538 ║  42645 │  39834 ║   74696 │   66007 ║   72037 │   64369 ║
║  34 │  34 ║     0 │     0 ║ 4419 │ 2571 ║   6142 │   5228 ║   29261 │   17846 ║   28452 │   17378 ║
║ 204 │ 204 ║  1638 │  1638 ║ 2064 │ 2488 ║  43371 │  41143 ║   75217 │   68021 ║   72239 │   65304 ║
║ 374 │ 374 ║  3276 │  3276 ║ 3379 │ 2607 ║  57179 │  51926 ║   84445 │   76323 ║   80016 │   73317 ║
║ 544 │ 544 ║  4915 │  4915 ║ 1517 │ 2409 ║  78908 │  65587 ║  113832 │  102386 ║  107544 │   97678 ║
║ 714 │ 714 ║  6553 │  6553 ║ 2609 │ 2521 ║  84990 │  84295 ║  124389 │  125591 ║  121957 │  119237 ║
║ 884 │ 884 ║  8192 │  8192 ║ 2015 │ 2483 ║ 119544 │ 110210 ║  147465 │  149467 ║  141859 │  139400 ║
║     │     ║       │       ║      │ sum: ║ 770082 │ 711992 ║ 1112459 │ 1051543 ║ 1073194 │ 1012738 ║
║     │     ║       │       ║      │  +%: ║    +44 │    +47 ║      +0 │      +0 ║      +3 │      +3 ║
╚═════╧═════╩═══════╧═══════╩══════╧══════╩════════╧════════╩═════════╧═════════╩═════════╧═════════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⡩⠛⠉⠉⠙⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠊⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠁⠀⠀⠀⠀⠀⢀⣀⠀⠀⠀⠀⠀⠈⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠉⠀⠀⠀⠀⠀⠀⢀⠜⠉⠉⠳⣄⠀⠀⠀⠀⠀⠀⠉⠒⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⠋⠀⠀⠀⠀⠈⢆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡠⠤⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠃⠀⠀⠀⠀⠀⠀⠈⠥⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⡠⠔⠊⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣐⠂⠀⠀⠀⠀⠀⠀⠀⠀⠈⠥⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠴⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢓⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⢄⣀⣀⣀⠤⢔⠕⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠪⡒⠤⣀⣀⣀⠤⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⢒⠭⠒⠒⠒⠒⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠒⠒⠒⠒⠢⠤⡉⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⡪⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢄⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⠞⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⡈⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⢤⠶⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⠈⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⣀⠤⠒⠒⣉⠭⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⢄⣈⠑⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⣀⠤⠒⢉⡠⠔⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⢄⡉⠑⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠔⣊⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⣀⠀⠈⠑⠒⠒⠒⠒⠒⠒⠒⢺
⡗⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T): 128
  14401 in 84832ms
╔═══════════╦═══════════════╦═════════════╦═══════════════╦═════════════════╦═════════════════╗
║           ║               ║             ║  BigInteger   ║     BigInt      ║      int[]      ║
║  length   ║   precision   ║    count    ║     T │ int[] ║      T │ int[]  ║      T │ int[]  ║
╠═════╤═════╬═══════╤═══════╬══════╤══════╬═══════╤═══════╬════════╤════════╬════════╤════════╣
║ 884 │ 884 ║ -8192 │ -8192 ║ 1573 │ 1291 ║  1584 │  1312 ║      2 │      5 ║      5 │      5 ║
║ 714 │ 714 ║ -6553 │ -6553 ║ 1027 │ 1227 ║  1359 │  1107 ║      2 │      5 ║      5 │      5 ║
║ 544 │ 544 ║ -4915 │ -4915 ║  621 │ 1043 ║   845 │   636 ║      2 │      5 ║      5 │      5 ║
║ 374 │ 374 ║ -3276 │ -3276 ║ 1635 │ 1189 ║   502 │   379 ║      2 │      5 ║      5 │      5 ║
║ 204 │ 204 ║ -1638 │ -1638 ║ 1082 │ 1192 ║   427 │   310 ║      2 │      5 ║      5 │      5 ║
║  34 │  34 ║     0 │     0 ║ 1475 │ 1467 ║   165 │   101 ║      2 │      5 ║      5 │      5 ║
║ 204 │ 204 ║  1638 │  1638 ║ 1032 │ 1142 ║   424 │   312 ║      2 │      5 ║      5 │      5 ║
║ 374 │ 374 ║  3276 │  3276 ║ 1635 │ 1189 ║   498 │   379 ║      2 │      5 ║      5 │      5 ║
║ 544 │ 544 ║  4915 │  4915 ║  621 │ 1043 ║   839 │   636 ║      2 │      5 ║      5 │      5 ║
║ 714 │ 714 ║  6553 │  6553 ║ 1027 │ 1227 ║  1354 │  1107 ║      2 │      5 ║      5 │      5 ║
║ 884 │ 884 ║  8192 │  8192 ║ 1573 │ 1291 ║  1580 │  1312 ║      2 │      5 ║      5 │      5 ║
║     │     ║       │       ║      │ sum: ║  9577 │  7591 ║     22 │     55 ║     55 │     55 ║
║     │     ║       │       ║      │  +%: ║    +0 │    +0 ║ +43431 │ +13701 ║ +17312 │ +13701 ║
╚═════╧═════╩═══════╧═══════╩══════╧══════╩═══════╧═══════╩════════╧════════╩════════╧════════╝
```

##### `mul(int,int)`

The `BigInteger` class does not have a constructor for unsigned `int`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `int` is accomplished with the [`BigIntegers.valueOf(int)`][BigIntegers] utility method.

This test engages the long multiplication algorithm for both `BigInt` and `BigInteger`.

```
[runtime performance]
mul(int,int)
  2640441 in 6859ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═══════════╦═══════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║  BigInt   ║   int[]   ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬═════╤═════╬═════╤═════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   55 │   48 ║  24 │  28 ║  22 │  24 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   59 │   49 ║  23 │  30 ║  22 │  26 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   54 │   52 ║  20 │  31 ║  19 │  29 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   48 │   50 ║  39 │  31 ║  36 │  28 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 140005 ║   47 │   48 ║  42 │  30 ║  36 │  28 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 139984 ║   47 │   58 ║  42 │  28 ║  36 │  26 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   49 │   65 ║  41 │  29 ║  36 │  28 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   52 │   61 ║  30 │  30 ║  27 │  29 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   59 │   55 ║  23 │  29 ║  22 │  27 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   58 │   49 ║  23 │  28 ║  23 │  25 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   53 │   49 ║  23 │  27 ║  21 │  25 ║
║    │    ║     │     ║        │   sum: ║  581 │  584 ║ 330 │ 321 ║ 300 │ 295 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +76 │ +81 ║ +93 │ +97 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩═════╧═════╩═════╧═════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⣉⣉⣉⣉⣉⣹
⡏⠉⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⣒⣒⠶⢖⣒⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠒⠒⠒⠒⠒⢒⣒⣒⣒⣒⣒⣒⣒⣊⣉⣉⣉⣁⣀⣀⣀⣀⣀⣀⣀⣸
⡗⠒⠒⠒⠒⠒⠒⠒⠒⠒⠉⠉⠀⠀⠀⠀⠀⠉⠢⡉⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠒⠒⠉⣁⡠⠤⠔⠒⠊⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡈⠒⢄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠔⠒⠒⠒⠒⠒⠒⠒⠒⠤⠤⠔⠒⠒⠉⠁⢀⡠⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢄⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠔⠒⠒⠒⠒⠒⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠒⠒⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠉⠑⠒⠢⠤⠤⠤⠤⠤⠤⠔⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⢄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠔⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(int,int)
  8361 in 454ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 1093 │ 1047 ║    12 │     9 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -16 │  -8 ║  688 │  676 ║    10 │     7 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -12 │  -6 ║  321 │  723 ║    12 │     9 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -8 │  -4 ║  721 │  816 ║    10 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -4 │  -2 ║  847 │  429 ║    10 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║   0 │   0 ║  405 │  408 ║     8 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║   4 │   2 ║  784 │  816 ║     8 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║   8 │   4 ║  721 │  723 ║     8 │     7 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║  12 │   6 ║  292 │  676 ║     9 │     9 ║     1 │     4 ║     4 │     4 ║
║ 35 │ 34 ║  16 │   8 ║  688 │  654 ║     9 │     8 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║  20 │  10 ║  701 │  293 ║    10 │    10 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   106 │    87 ║    11 │    34 ║    34 │    34 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║  +863 │  +155 ║  +211 │  +155 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(int)`

This test engages the long multiplication algorithm for both `BigInt` and `BigInteger`.

```
[runtime performance]
mul(int)
  2640441 in 6574ms
╔═════════╦═══════════╦═════════════════╦═════════════╦════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt   ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤═════╬══════╤══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 455921 │ 375921 ║   60 │   60 ║   29 │  33 ║   17 │   27 ║
║ 35 │ 34 ║ -16 │  -8 ║ 263900 │ 259900 ║   71 │   64 ║   27 │  32 ║   17 │   24 ║
║ 35 │ 34 ║ -12 │  -6 ║ 119921 │ 267921 ║   63 │   69 ║   23 │  33 ║   15 │   24 ║
║ 34 │ 34 ║  -8 │  -4 ║ 239921 │ 275984 ║   55 │   73 ║   36 │  33 ║   34 │   24 ║
║ 34 │ 34 ║  -4 │  -2 ║ 240047 │ 140005 ║   68 │   60 ║   40 │  32 ║   37 │   23 ║
║ 34 │ 34 ║   0 │   0 ║ 120005 │ 139984 ║   54 │   62 ║   35 │  29 ║   32 │   22 ║
║ 34 │ 34 ║   4 │   2 ║ 239984 │ 275984 ║   66 │   67 ║   42 │  31 ║   44 │   24 ║
║ 34 │ 34 ║   8 │   4 ║ 239921 │ 267921 ║   60 │   62 ║   31 │  31 ║   24 │   23 ║
║ 35 │ 34 ║  12 │   6 ║ 127900 │ 259900 ║   66 │   58 ║   25 │  30 ║   15 │   24 ║
║ 35 │ 34 ║  16 │   8 ║ 279900 │ 251900 ║   68 │   54 ║   27 │  31 ║   17 │   24 ║
║ 36 │ 35 ║  20 │  10 ║ 311921 │ 123921 ║   60 │   52 ║   29 │  30 ║   17 │   23 ║
║    │    ║     │     ║        │   sum: ║  691 │  681 ║  344 │ 345 ║  269 │  262 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +100 │ +97 ║ +156 │ +159 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧═════╩══════╧══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⠭⠭⠭⠭⠭⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⡩⠭⠭⠭⣉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡗⠒⠒⠒⠊⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠒⠒⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⡀⢀⣀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠤⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⡠⠤⠤⠒⠒⠊⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠤⠒⠒⠤⠤⠤⠤⢄⣈⣁⣀⣀⡠⠤⠤⠤⠤⠔⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠒⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠒⠒⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣸
⣇⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠊⠉⠉⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⠤⠤⠔⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠈⠉⠒⠢⢄⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠤⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⣀⣀⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(int)
  8361 in 251ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 35 ║ -20 │ -10 ║ 1093 │ 1047 ║    12 │     9 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -16 │  -8 ║  688 │  676 ║    10 │     7 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -12 │  -6 ║  321 │  723 ║    12 │     9 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -8 │  -4 ║  721 │  816 ║    10 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -4 │  -2 ║  847 │  429 ║    10 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║   0 │   0 ║  405 │  408 ║     8 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║   4 │   2 ║  784 │  816 ║     8 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║   8 │   4 ║  721 │  723 ║     8 │     7 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║  12 │   6 ║  292 │  676 ║     9 │     9 ║     1 │     4 ║     4 │     4 ║
║ 35 │ 34 ║  16 │   8 ║  688 │  654 ║     9 │     8 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║  20 │  10 ║  701 │  293 ║    10 │    10 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   106 │    87 ║    11 │    34 ║    34 │    34 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║  +863 │  +155 ║  +211 │  +155 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(int,long)`

The `BigInteger` class does not have a constructor for unsigned `long`. Therefore, for this test, the creation of a `BigInteger` from an unsigned `long` is accomplished with the [`BigIntegers.valueOf(long)`][BigIntegers] utility method.

This test engages the long multiplication algorithm for both `BigInt` and `BigInteger`.

```
[runtime performance]
mul(int,long)
  946929 in 3412ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║   95 │  125 ║   26 │   30 ║   35 │   41 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║   96 │  124 ║   25 │   31 ║   33 │   41 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║   88 │  125 ║   25 │   34 ║   38 │   45 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   92 │  125 ║   37 │   34 ║   52 │   47 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   92 │  128 ║   37 │   35 ║   44 │   47 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   93 │   67 ║   45 │   26 ║   47 │   29 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   90 │   60 ║   42 │   25 ║   46 │   29 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║   88 │   53 ║   37 │   27 ║   51 │   31 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║   89 │   62 ║   21 │   31 ║   32 │   41 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║   98 │   61 ║   26 │   32 ║   33 │   42 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║   93 │   58 ║   25 │   30 ║   33 │   40 ║
║    │    ║     │     ║        │   sum: ║ 1014 │  988 ║  346 │  335 ║  444 │  433 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +193 │ +194 ║ +128 │ +128 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⣏⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⡩⠭⠭⠭⠭⢍⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⡩⠭⠭⠭⠭⠭⠭⠭⠭⢽
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠒⠒⠢⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠤⠤⠤⠔⠒⠒⠒⠒⠢⠤⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠤⢄⣀⡀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠢⠤⠤⠤⠤⠤⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⠤⠤⠤⠒⠊⠉⠁⠀⠀⠀⠀⠈⠉⠒⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠒⠒⠒⠒⠒⠒⠒⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠒⠒⠒⠒⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(int,long)
  13117 in 624ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 1155 │ 1479 ║    38 │    24 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║ -30 │ -15 ║ 1204 │  860 ║    36 │    24 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║ -22 │ -11 ║  968 │ 1270 ║    20 │    14 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -15 │  -7 ║ 1291 │  951 ║    12 │     8 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -7 │  -3 ║ 1018 │ 1510 ║    11 │     9 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║ 1202 │  806 ║     9 │     8 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   7 │   3 ║  972 │ 1338 ║    10 │     9 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║  15 │   7 ║ 1268 │  908 ║    10 │     9 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║  22 │  11 ║  924 │ 1200 ║    26 │    19 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║  30 │  15 ║ 1204 │  844 ║    35 │    24 ║     1 │     3 ║     3 │     3 ║
║ 38 │ 36 ║  38 │  19 ║  811 │  851 ║    38 │    25 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   245 │   173 ║    11 │    31 ║    31 │    31 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +2127 │  +458 ║  +690 │  +458 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(long)`

This test engages the long multiplication algorithm for both `BigInt` and `BigInteger`.

```
[runtime performance]
mul(long)
  946929 in 3268ms
╔═════════╦═══════════╦═════════════════╦═════════════╦═════════════╦═════════════╗
║ length  ║ precision ║      count      ║ BigInteger  ║   BigInt    ║    int[]    ║
╠════╤════╬═════╤═════╬════════╤════════╬══════╤══════╬══════╤══════╬══════╤══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 116723 │ 117523 ║   76 │   74 ║   26 │   37 ║   21 │   30 ║
║ 37 │ 35 ║ -30 │ -15 ║ 103900 │  72700 ║   73 │   80 ║   22 │   37 ║   19 │   30 ║
║ 36 │ 35 ║ -22 │ -11 ║  69946 │  99946 ║   72 │   71 ║   31 │   30 ║   25 │   23 ║
║ 35 │ 34 ║ -15 │  -7 ║  91123 │  77123 ║   74 │   74 ║   46 │   28 ║   34 │   22 ║
║ 34 │ 34 ║  -7 │  -3 ║  68392 │ 105684 ║   68 │   76 ║   34 │   29 ║   25 │   22 ║
║ 34 │ 34 ║   0 │   0 ║  68576 │  53284 ║   72 │   76 ║   35 │   34 ║   26 │   19 ║
║ 34 │ 34 ║   7 │   3 ║  68346 │ 103946 ║   72 │   72 ║   39 │   24 ║   27 │   19 ║
║ 35 │ 34 ║  15 │   7 ║  91100 │  75900 ║   74 │   65 ║   49 │   25 ║   36 │   20 ║
║ 36 │ 35 ║  22 │  11 ║  71900 │  98300 ║   77 │   75 ║   24 │   34 ║   19 │   27 ║
║ 37 │ 35 ║  30 │  15 ║ 107100 │  71500 ║   72 │   77 ║   24 │   39 ║   19 │   30 ║
║ 38 │ 36 ║  38 │  19 ║  88723 │  69923 ║   75 │   69 ║   28 │   37 ║   23 │   28 ║
║    │    ║     │     ║        │   sum: ║  805 │  809 ║  358 │  354 ║  274 │  270 ║
║    │    ║     │     ║        │    +%: ║   +0 │   +0 ║ +124 │ +128 ║ +193 │ +199 ║
╚════╧════╩═════╧═════╩════════╧════════╩══════╧══════╩══════╧══════╩══════╧══════╝
⣏⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⡩⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⠭⢍⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⡩⠭⠭⠭⠭⠭⣉⣉⣉⣉⣉⣉⣉⠉⠉⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠢⠤⠤⠤⠤⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⠤⣀⠤⠤⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⣇⣀⣀⡠⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠒⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠒⠒⠒⠢⢄⣀⠀⠀⠀⠀⠀⠀⢀⡠⠤⠒⠒⠒⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⠤⠤⠔⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠒⠒⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⢄⣀⣀⣀⠔⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠤⠤⠤⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⡠⠤⠤⠤⠤⣀⣀⣀⣀⠀⠀⠀⠀⢀⣀⣀⣀⣀⠤⠤⠤⠤⠒⠒⠒⠉⠉⠉⠒⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡗⠒⠒⠒⠒⠒⠒⠒⠒⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠒⠒⠒⠊⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(long)
  13117 in 567ms
╔═════════╦═══════════╦═════════════╦═══════════════╦═══════════════╦═══════════════╗
║         ║           ║             ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length  ║ precision ║    count    ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬══════╤══════╬═══════╤═══════╬═══════╤═══════╬═══════╤═══════╣
║ 38 │ 36 ║ -38 │ -19 ║ 1155 │ 1479 ║    36 │    22 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║ -30 │ -15 ║ 1204 │  860 ║    35 │    22 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║ -22 │ -11 ║  968 │ 1270 ║    19 │    13 ║     1 │     3 ║     3 │     3 ║
║ 35 │ 34 ║ -15 │  -7 ║ 1291 │  951 ║    10 │     7 ║     1 │     3 ║     3 │     3 ║
║ 34 │ 34 ║  -7 │  -3 ║ 1018 │ 1510 ║    10 │     7 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   0 │   0 ║ 1202 │  806 ║     8 │     6 ║     1 │     2 ║     2 │     2 ║
║ 34 │ 34 ║   7 │   3 ║  972 │ 1338 ║     8 │     7 ║     1 │     2 ║     2 │     2 ║
║ 35 │ 34 ║  15 │   7 ║ 1268 │  908 ║     8 │     7 ║     1 │     3 ║     3 │     3 ║
║ 36 │ 35 ║  22 │  11 ║  924 │ 1200 ║    25 │    18 ║     1 │     3 ║     3 │     3 ║
║ 37 │ 35 ║  30 │  15 ║ 1204 │  844 ║    33 │    22 ║     1 │     3 ║     3 │     3 ║
║ 38 │ 36 ║  38 │  19 ║  811 │  851 ║    36 │    24 ║     1 │     3 ║     3 │     3 ║
║    │    ║     │     ║      │ sum: ║   228 │   155 ║    11 │    30 ║    30 │    30 ║
║    │    ║     │     ║      │  +%: ║    +0 │    +0 ║ +1972 │  +416 ║  +660 │  +416 ║
╚════╧════╩═════╧═════╩══════╧══════╩═══════╧═══════╩═══════╧═══════╩═══════╧═══════╝
```

##### `mul(T,T): 1`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

The behavior of square multiplication is similar to the behavior of regular multiplication (where the input argument is not the same instance as the target object). It is, however, interesting to note that the Karatsuba algorithm runs faster for the "square" use-case, as the equality of `x` and `y` have a better change of allowing for in-place calculations.

```
[runtime performance]
mul(T,T): 1
  33393 in 208ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     40 ║       -64 ║  3303 ║         58 ║     77 ║    70 ║
║     39 ║       -51 ║  2997 ║         58 ║     73 ║    61 ║
║     38 ║       -38 ║  2766 ║         41 ║     51 ║    47 ║
║     36 ║       -25 ║  3022 ║         34 ║     43 ║    36 ║
║     35 ║       -12 ║  3047 ║         25 ║     33 ║    30 ║
║     34 ║         0 ║  2610 ║         29 ║     37 ║    29 ║
║     35 ║        12 ║  2972 ║         25 ║     34 ║    30 ║
║     36 ║        25 ║  3047 ║         38 ║     53 ║    40 ║
║     38 ║        38 ║  2741 ║         48 ║     54 ║    48 ║
║     39 ║        51 ║  2997 ║         60 ║     80 ║    64 ║
║     40 ║        64 ║  2791 ║         58 ║     77 ║    70 ║
║        ║           ║  sum: ║        474 ║    612 ║   525 ║
║        ║           ║   +%: ║        +29 ║     +0 ║   +16 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⠝⠋⠉⣉⣉⠭⠛⠉⢉⣉⠭⠛⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⠻⣭⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡏⠉⠉⠉⠉⠉⠉⢉⡩⠝⠒⠊⠉⠉⠀⠀⣀⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⡀⠀⠀⠀⠀⢀⡠⠤⠤⣀⠀⠀⠀⠑⠫⡒⠢⠤⠤⠤⠤⠤⠒⠒⠊⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⢀⠤⠊⠁⠀⠀⠀⠀⠀⢀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠒⠒⠉⠁⠀⠀⠀⠀⠑⢄⠀⠀⠀⠈⠑⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⢀⡠⠊⠁⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⠀⠀⠀⠀⠀⠀⠉⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠁⠀⠀⠀⠀⠀⠀⠀⣀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⡀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⢀⣀⣀⡠⠤⠒⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⣀⠤⠔⠊⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T,T): 1
  17009 in 823ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     40 ║       -64 ║  1639 ║    38 │    22 ║     1 │     3 ║     3 │     3 ║
║     39 ║       -51 ║  1461 ║    37 │    22 ║     1 │     3 ║     3 │     3 ║
║     38 ║       -38 ║  1358 ║    35 │    21 ║     1 │     3 ║     3 │     3 ║
║     36 ║       -25 ║  1486 ║    24 │    15 ║     1 │     3 ║     3 │     3 ║
║     35 ║       -12 ║  1511 ║     8 │     5 ║     1 │     3 ║     3 │     3 ║
║     34 ║         0 ║  1330 ║     6 │     5 ║     1 │     3 ║     3 │     3 ║
║     35 ║        12 ║  1436 ║     6 │     6 ║     1 │     3 ║     3 │     3 ║
║     36 ║        25 ║  1511 ║    25 │    17 ║     1 │     3 ║     3 │     3 ║
║     38 ║        38 ║  1333 ║    33 │    21 ║     1 │     3 ║     3 │     3 ║
║     39 ║        51 ║  1461 ║    35 │    22 ║     1 │     3 ║     3 │     3 ║
║     40 ║        64 ║  1383 ║    37 │    23 ║     1 │     3 ║     3 │     3 ║
║        ║           ║  sum: ║   284 │   179 ║    11 │    33 ║    33 │    33 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +2481 │  +442 ║  +760 │  +442 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝
```

##### `mul(T,T): 2`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

The behavior of square multiplication is similar to the behavior of regular multiplication (where the input argument is not the same instance as the target object). It is, however, interesting to note that the Karatsuba algorithm runs faster for the "square" use-case, as the equality of `x` and `y` have a better change of allowing for in-place calculations.

```
[runtime performance]
mul(T,T): 2
  107377 in 1074ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     47 ║      -128 ║ 12663 ║        127 ║    161 ║   158 ║
║     44 ║      -102 ║  9605 ║         94 ║    126 ║   122 ║
║     41 ║       -76 ║  9454 ║         73 ║     97 ║    84 ║
║     39 ║       -51 ║  9166 ║         53 ║     70 ║    57 ║
║     36 ║       -25 ║  8398 ║         38 ║     43 ║    38 ║
║     34 ║         0 ║  8523 ║         28 ║     36 ║    32 ║
║     36 ║        25 ║  8348 ║         40 ║     45 ║    43 ║
║     39 ║        51 ║  9191 ║         55 ║     72 ║    61 ║
║     41 ║        76 ║  9501 ║         73 ║     98 ║    87 ║
║     44 ║       102 ║  9693 ║         97 ║    137 ║   121 ║
║     47 ║       128 ║ 11735 ║        130 ║    174 ║   155 ║
║        ║           ║  sum: ║        808 ║   1059 ║   958 ║
║        ║           ║   +%: ║        +31 ║     +0 ║   +10 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⡩⠭⠝⠛⢉⡩⠝⠋⠉⠉⢉⡩⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⢍⡉⠙⠫⠭⣉⠉⠛⠛⠭⠭⢍⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠔⠒⠊⠉⠀⢀⡠⠔⠊⠁⠀⠀⢀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⢄⠀⠀⠉⠒⠢⢄⡀⠀⠀⠈⠉⠒⠒⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⢀⣀⠤⠒⠊⠁⠀⠀⠀⠀⢀⠤⠊⠁⠀⢀⣀⠤⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠢⢄⣀⠀⠈⠑⠢⢄⡀⠀⠀⠀⠀⠀⠉⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⠤⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⡠⠔⠁⣀⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠈⠑⠢⣀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⣀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⢊⡠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⢄⡀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⣀⢤⡪⠕⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⡀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⣀⣤⡲⠝⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⡀⠀⠀⠀⠉⠑⠢⠤⠤⠤⠤⠤⠤⠤⢼
⡷⠛⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T,T): 2
  54001 in 3320ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     47 ║      -128 ║  6319 ║    41 │    22 ║     1 │     3 ║     3 │     3 ║
║     44 ║      -102 ║  4765 ║    40 │    22 ║     1 │     3 ║     3 │     3 ║
║     41 ║       -76 ║  4702 ║    38 │    22 ║     1 │     3 ║     3 │     3 ║
║     39 ║       -51 ║  4558 ║    34 │    20 ║     1 │     3 ║     3 │     3 ║
║     36 ║       -25 ║  4174 ║    21 │    13 ║     1 │     3 ║     3 │     3 ║
║     34 ║         0 ║  4299 ║     6 │     5 ║     1 │     3 ║     3 │     3 ║
║     36 ║        25 ║  4124 ║    21 │    14 ║     1 │     3 ║     3 │     3 ║
║     39 ║        51 ║  4583 ║    33 │    20 ║     1 │     3 ║     3 │     3 ║
║     41 ║        76 ║  4713 ║    36 │    22 ║     1 │     3 ║     3 │     3 ║
║     44 ║       102 ║  4809 ║    38 │    22 ║     1 │     3 ║     3 │     3 ║
║     47 ║       128 ║  5855 ║    40 │    23 ║     1 │     3 ║     3 │     3 ║
║        ║           ║  sum: ║   348 │   205 ║    11 │    33 ║    33 │    33 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +3063 │  +521 ║  +954 │  +521 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝
```

##### `mul(T,T): 4`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

The behavior of square multiplication is similar to the behavior of regular multiplication (where the input argument is not the same instance as the target object). It is, however, interesting to note that the Karatsuba algorithm runs faster for the "square" use-case, as the equality of `x` and `y` have a better change of allowing for in-place calculations.

```
[runtime performance]
mul(T,T): 4
  239385 in 5405ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     60 ║      -256 ║ 27655 ║        243 ║    413 ║   396 ║
║     55 ║      -204 ║ 23817 ║        221 ║    315 ║   298 ║
║     49 ║      -153 ║ 22290 ║        163 ║    215 ║   201 ║
║     44 ║      -102 ║ 19226 ║        104 ║    142 ║   127 ║
║     39 ║       -51 ║ 20110 ║         62 ║     81 ║    67 ║
║     34 ║         0 ║ 14411 ║         43 ║     54 ║    45 ║
║     39 ║        51 ║ 17788 ║         62 ║     84 ║    72 ║
║     44 ║       102 ║ 21811 ║        117 ║    155 ║   140 ║
║     49 ║       153 ║ 19705 ║        166 ║    223 ║   203 ║
║     55 ║       204 ║ 23817 ║        219 ║    320 ║   299 ║
║     60 ║       256 ║ 27655 ║        246 ║    425 ║   397 ║
║        ║           ║  sum: ║       1646 ║   2427 ║  2245 ║
║        ║           ║   +%: ║        +47 ║     +0 ║    +8 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⡩⠭⠝⠛⢉⡩⠝⢋⡩⠝⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⠫⠭⣛⠭⣉⠙⠛⠭⠭⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⠤⠒⠊⠉⠀⣀⡠⠔⢊⡡⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠭⣒⠤⢄⡀⠈⠉⠑⠒⠢⠤⢄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⣀⣀⣀⣀⣀⣀⣀⣀⡠⠤⠔⠒⠉⠁⠀⠀⠀⣀⠤⢒⣉⠤⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠬⣉⡒⠢⠤⣀⡀⠀⠀⠈⠉⠑⠒⠢⠤⠤⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⢒⡩⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠒⠤⣈⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⢒⠥⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⡠⢒⡡⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⡠⠔⡩⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⣈⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⡠⠔⡩⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⡈⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⠔⡪⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⡈⠑⠤⣀⣀⣀⣀⣀⣀⣀⣸
⡗⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢄⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T,T): 4
  117781 in 9411ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     60 ║      -256 ║ 13019 ║    47 │    25 ║     1 │     3 ║     3 │     3 ║
║     55 ║      -204 ║ 12005 ║    44 │    24 ║     1 │     3 ║     3 │     3 ║
║     49 ║      -153 ║ 11460 ║    42 │    23 ║     1 │     3 ║     3 │     3 ║
║     44 ║      -102 ║  8748 ║    39 │    22 ║     1 │     3 ║     3 │     3 ║
║     39 ║       -51 ║  9046 ║    34 │    20 ║     1 │     3 ║     3 │     3 ║
║     34 ║         0 ║  8989 ║     9 │     7 ║     1 │     3 ║     3 │     3 ║
║     39 ║        51 ║  8182 ║    32 │    20 ║     1 │     3 ║     3 │     3 ║
║     44 ║       102 ║ 10029 ║    37 │    22 ║     1 │     3 ║     3 │     3 ║
║     49 ║       153 ║ 10179 ║    40 │    24 ║     1 │     3 ║     3 │     3 ║
║     55 ║       204 ║ 12005 ║    42 │    24 ║     1 │     3 ║     3 │     3 ║
║     60 ║       256 ║ 13019 ║    45 │    25 ║     1 │     3 ║     3 │     3 ║
║        ║           ║  sum: ║   411 │   236 ║    11 │    33 ║    33 │    33 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +3636 │  +615 ║ +1145 │  +615 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝
```

##### `mul(T,T): 8`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

The behavior of square multiplication is similar to the behavior of regular multiplication (where the input argument is not the same instance as the target object). It is, however, interesting to note that the Karatsuba algorithm runs faster for the "square" use-case, as the equality of `x` and `y` have a better change of allowing for in-place calculations.

```
[runtime performance]
mul(T,T): 8
  374241 in 18199ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║     87 ║      -512 ║ 33715 ║        688 ║   1294 ║  1256 ║
║     76 ║      -409 ║ 37557 ║        469 ║    879 ║   855 ║
║     65 ║      -307 ║ 33986 ║        308 ║    554 ║   531 ║
║     55 ║      -204 ║ 30894 ║        202 ║    328 ║   308 ║
║     44 ║      -102 ║ 36174 ║         94 ║    149 ║   130 ║
║     34 ║         0 ║ 28539 ║         47 ║     75 ║    62 ║
║     44 ║       102 ║ 36124 ║         97 ║    151 ║   132 ║
║     55 ║       204 ║ 35039 ║        213 ║    348 ║   322 ║
║     65 ║       307 ║ 29841 ║        316 ║    576 ║   548 ║
║     76 ║       409 ║ 37557 ║        470 ║    885 ║   851 ║
║     87 ║       512 ║ 33715 ║        684 ║   1308 ║  1260 ║
║        ║           ║  sum: ║       3588 ║   6547 ║  6255 ║
║        ║           ║   +%: ║        +82 ║     +0 ║    +4 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⡩⠭⠭⠝⠛⠛⣉⣩⣭⠿⠿⠛⠛⠉⠉⠉⠉⠉⠉⠉⠉⠛⠛⠻⠿⣭⣍⣉⠛⠛⠫⠭⠭⣉⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠤⠤⠔⠒⠒⠊⠉⠉⠁⠀⠀⠀⢀⣠⣔⠶⠛⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠛⠶⣤⣀⡀⠀⠀⠀⠉⠉⠉⠑⠒⠒⠢⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⢀⣀⠤⠤⠒⠒⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⡴⠞⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠚⠵⣢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠑⠒⠢⠤⢄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⡴⠞⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠚⠵⣢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⢤⠶⠓⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠫⢖⡤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⢤⠮⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠚⠵⡢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⢀⣠⠮⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠳⣢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⢀⣠⡲⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠵⡢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⡠⡴⠕⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢕⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⢔⠝⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠪⡒⢄⣀⣀⣀⣀⣀⣀⣀⣸
⡏⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T,T): 8
  186081 in 21146ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║     87 ║      -512 ║ 16597 ║    51 │    26 ║     1 │     3 ║     3 │     3 ║
║     76 ║      -409 ║ 16539 ║    50 │    26 ║     1 │     3 ║     3 │     3 ║
║     65 ║      -307 ║ 18696 ║    47 │    25 ║     1 │     3 ║     3 │     3 ║
║     55 ║      -204 ║ 16796 ║    44 │    23 ║     1 │     3 ║     3 │     3 ║
║     44 ║      -102 ║ 17086 ║    38 │    21 ║     1 │     3 ║     3 │     3 ║
║     34 ║         0 ║ 13603 ║    22 │    14 ║     1 │     3 ║     3 │     3 ║
║     44 ║       102 ║ 17036 ║    36 │    21 ║     1 │     3 ║     3 │     3 ║
║     55 ║       204 ║ 18915 ║    42 │    23 ║     1 │     3 ║     3 │     3 ║
║     65 ║       307 ║ 16577 ║    46 │    26 ║     1 │     3 ║     3 │     3 ║
║     76 ║       409 ║ 16539 ║    48 │    26 ║     1 │     3 ║     3 │     3 ║
║     87 ║       512 ║ 16597 ║    49 │    26 ║     1 │     3 ║     3 │     3 ║
║        ║           ║  sum: ║   473 │   257 ║    11 │    33 ║    33 │    33 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +4200 │  +678 ║ +1333 │  +678 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝
```

##### `mul(T,T): 16`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

The behavior of square multiplication is similar to the behavior of regular multiplication (where the input argument is not the same instance as the target object). It is, however, interesting to note that the Karatsuba algorithm runs faster for the "square" use-case, as the equality of `x` and `y` have a better change of allowing for in-place calculations.

```
[runtime performance]
mul(T,T): 16
  313449 in 37439ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║    140 ║     -1024 ║ 27051 ║       2138 ║   4593 ║  4536 ║
║    119 ║      -819 ║ 26053 ║       1430 ║   2903 ║  2880 ║
║     97 ║      -614 ║ 30285 ║        841 ║   1659 ║  1650 ║
║     76 ║      -409 ║ 30855 ║        436 ║    867 ║   863 ║
║     55 ║      -204 ║ 31622 ║        175 ║    349 ║   343 ║
║     34 ║         0 ║ 20667 ║         55 ║    141 ║   139 ║
║     55 ║       204 ║ 31572 ║        177 ║    353 ║   346 ║
║     76 ║       409 ║ 30855 ║        435 ║    870 ║   866 ║
║     97 ║       614 ║ 30285 ║        842 ║   1651 ║  1653 ║
║    119 ║       819 ║ 26053 ║       1424 ║   2902 ║  2886 ║
║    140 ║      1024 ║ 27051 ║       2135 ║   4609 ║  4549 ║
║        ║           ║  sum: ║      10088 ║  20897 ║ 20711 ║
║        ║           ║   +%: ║       +107 ║     +0 ║    +0 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣉⣉⣉⠭⠭⠭⠝⠛⠛⠛⠋⢉⣉⡭⠭⠝⠛⠛⠛⠋⠉⠉⠙⠛⠛⠛⠫⠭⠭⣉⣉⠉⠛⠛⠛⠛⠭⠭⠭⢍⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡠⠤⠤⠒⠒⠊⠉⠉⠀⠀⠀⠀⠀⠀⣀⠤⠔⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠲⠤⣀⡀⠀⠀⠀⠀⠀⠉⠉⠉⠒⠒⠢⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⢀⣀⣀⠤⠤⠒⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠚⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠢⠤⢄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡏⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠚⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⡠⠖⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⢀⠴⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⣠⠖⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠓⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⢀⡠⠚⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⢦⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡷⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⠢⣤⣤⣤⣤⣤⣤⣤⣼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T,T): 16
  148941 in 31098ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║    140 ║     -1024 ║ 13343 ║    58 │    28 ║     1 │     3 ║     3 │     3 ║
║    119 ║      -819 ║ 11103 ║    55 │    27 ║     1 │     3 ║     3 │     3 ║
║     97 ║      -614 ║ 11741 ║    52 │    27 ║     1 │     3 ║     3 │     3 ║
║     76 ║      -409 ║ 15553 ║    49 │    25 ║     1 │     3 ║     3 │     3 ║
║     55 ║      -204 ║ 17022 ║    43 │    23 ║     1 │     3 ║     3 │     3 ║
║     34 ║         0 ║ 10367 ║    30 │    17 ║     1 │     3 ║     3 │     3 ║
║     55 ║       204 ║ 16972 ║    41 │    23 ║     1 │     3 ║     3 │     3 ║
║     76 ║       409 ║ 15553 ║    47 │    25 ║     1 │     3 ║     3 │     3 ║
║     97 ║       614 ║ 11741 ║    50 │    27 ║     1 │     3 ║     3 │     3 ║
║    119 ║       819 ║ 11103 ║    53 │    27 ║     1 │     3 ║     3 │     3 ║
║    140 ║      1024 ║ 13343 ║    56 │    28 ║     1 │     3 ║     3 │     3 ║
║        ║           ║  sum: ║   534 │   277 ║    11 │    33 ║    33 │    33 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +4754 │  +739 ║ +1518 │  +739 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝
```

##### `mul(T,T): 32`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

The behavior of square multiplication is similar to the behavior of regular multiplication (where the input argument is not the same instance as the target object). It is, however, interesting to note that the Karatsuba algorithm runs faster for the "square" use-case, as the equality of `x` and `y` have a better change of allowing for in-place calculations.

```
[runtime performance]
mul(T,T): 32
  168969 in 55276ms
╔════════╦═══════════╦═══════╦════════════╦════════╦═══════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[] ║
╠════════╬═══════════╬═══════╬════════════╬════════╬═══════╣
║    246 ║     -2048 ║ 13111 ║       8337 ║  16554 ║ 16404 ║
║    204 ║     -1638 ║ 14229 ║       5793 ║  10413 ║ 10282 ║
║    161 ║     -1228 ║ 13965 ║       3105 ║   6498 ║  6392 ║
║    119 ║      -819 ║ 17323 ║       1479 ║   3078 ║  3032 ║
║     76 ║      -409 ║ 19062 ║        383 ║    754 ║   755 ║
║     34 ║         0 ║ 12539 ║        109 ║    210 ║   209 ║
║     76 ║       409 ║ 19012 ║        388 ║    761 ║   754 ║
║    119 ║       819 ║ 17323 ║       1478 ║   3084 ║  3049 ║
║    161 ║      1228 ║ 13965 ║       3091 ║   6515 ║  6415 ║
║    204 ║      1638 ║ 14229 ║       5634 ║  10407 ║ 10284 ║
║    246 ║      2048 ║ 13111 ║       8519 ║  16564 ║ 16421 ║
║        ║           ║  sum: ║      38316 ║  74838 ║ 73997 ║
║        ║           ║   +%: ║        +95 ║     +0 ║    +1 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩═══════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⣉⡩⠭⠭⠝⠛⠛⠛⣉⡩⠭⠛⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⠛⠫⠭⣉⠛⠛⠛⠫⠭⠭⢍⣉⣉⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⣀⠤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠢⢄⡀⠀⠀⠀⠀⠈⠉⠉⠒⠒⠢⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⢀⣀⣀⠤⠔⠒⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡤⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠓⠦⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠢⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠒⠒⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠴⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠒⠒⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⠔⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠲⢤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⠴⠚⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠦⣄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⢀⡤⠞⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠓⢦⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⣠⠞⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠓⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⢀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⢦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⢀⡴⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡷⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠳⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T,T): 32
  83069 in 42098ms
╔════════╦═══════════╦═══════╦═══════════════╦═══════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║    BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[] ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬═══════════════╬═══════════════╣
║    246 ║     -2048 ║  7779 ║    97 │    54 ║     1 │     3 ║     3 │     3 ║
║    204 ║     -1638 ║  6779 ║    84 │    46 ║     1 │     3 ║     3 │     3 ║
║    161 ║     -1228 ║  6769 ║    62 │    31 ║     1 │     3 ║     3 │     3 ║
║    119 ║      -819 ║  8565 ║    55 │    27 ║     1 │     3 ║     3 │     3 ║
║     76 ║      -409 ║  7726 ║    48 │    25 ║     1 │     3 ║     3 │     3 ║
║     34 ║         0 ║  6783 ║    37 │    21 ║     1 │     3 ║     3 │     3 ║
║     76 ║       409 ║  7676 ║    46 │    25 ║     1 │     3 ║     3 │     3 ║
║    119 ║       819 ║  8565 ║    53 │    27 ║     1 │     3 ║     3 │     3 ║
║    161 ║      1228 ║  6769 ║    60 │    31 ║     1 │     3 ║     3 │     3 ║
║    204 ║      1638 ║  6779 ║    82 │    46 ║     1 │     3 ║     3 │     3 ║
║    246 ║      2048 ║  7779 ║    95 │    54 ║     1 │     3 ║     3 │     3 ║
║        ║           ║  sum: ║   719 │   387 ║    11 │    33 ║    33 │    33 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +6436 │ +1072 ║ +2078 │ +1072 ║
╚════════╩═══════════╩═══════╩═══════════════╩═══════════════╩═══════════════╝
```

##### `mul(T,T): 64`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

The behavior of square multiplication is similar to the behavior of regular multiplication (where the input argument is not the same instance as the target object). It is, however, interesting to note that the Karatsuba algorithm runs faster for the "square" use-case, as the equality of `x` and `y` have a better change of allowing for in-place calculations.

```
[runtime performance]
mul(T,T): 64
  79361 in 75043ms
╔════════╦═══════════╦═══════╦════════════╦════════╦════════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[]  ║
╠════════╬═══════════╬═══════╬════════════╬════════╬════════╣
║    459 ║     -4096 ║  4611 ║      30618 ║  64026 ║  63898 ║
║    374 ║     -3276 ║  7573 ║      20108 ║  39956 ║  39611 ║
║    289 ║     -2457 ║  5897 ║      13262 ║  23817 ║  23682 ║
║    204 ║     -1638 ║  7651 ║       4922 ║   9936 ║   9786 ║
║    119 ║      -819 ║  8526 ║       1189 ║   2482 ║   2451 ║
║     34 ║         0 ║  9795 ║        190 ║    355 ║    358 ║
║    119 ║       819 ║  8476 ║       1197 ║   2482 ║   2466 ║
║    204 ║      1638 ║  7651 ║       4939 ║   9950 ║   9841 ║
║    289 ║      2457 ║  5897 ║      13078 ║  23826 ║  23658 ║
║    374 ║      3276 ║  7573 ║      20033 ║  39826 ║  39711 ║
║    459 ║      4096 ║  4611 ║      31007 ║  64158 ║  63942 ║
║        ║           ║  sum: ║     140543 ║ 280814 ║ 279404 ║
║        ║           ║   +%: ║        +99 ║     +0 ║     +0 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩════════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⡩⠭⠭⠛⠛⠛⢛⣉⠭⠽⠛⠛⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠛⠛⠫⠭⢍⣙⠛⠛⠛⠭⠭⠭⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⣀⠤⠤⠒⠒⠉⠁⠀⠀⢀⡠⠴⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠓⠢⢄⣀⠀⠀⠀⠉⠑⠒⠢⠤⢄⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⢀⣀⡠⠤⠔⠒⠒⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⣀⠴⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠢⢄⡀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠑⠒⠒⠤⠤⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠒⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠖⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠓⢤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠖⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠓⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⡠⠖⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⢀⠴⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⣠⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⡠⠚⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢤⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡧⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
mul(T,T): 64
  42393 in 57114ms
╔════════╦═══════════╦═══════╦═══════════════╦════════════════╦═══════════════╗
║        ║           ║       ║  BigInteger   ║     BigInt     ║     int[]     ║
║ length ║ precision ║ count ║     T │ int[] ║     T │ int[]  ║     T │ int[] ║
╠════════╬═══════════╬═══════╬═══════════════╬════════════════╬═══════════════╣
║    459 ║     -4096 ║  3395 ║   317 │   232 ║      1 │     3 ║     3 │     3 ║
║    374 ║     -3276 ║  3227 ║   191 │   129 ║      1 │     3 ║     3 │     3 ║
║    289 ║     -2457 ║  3323 ║   123 │    74 ║      1 │     3 ║     3 │     3 ║
║    204 ║     -1638 ║  4465 ║    80 │    43 ║      1 │     3 ║     3 │     3 ║
║    119 ║      -819 ║  3980 ║    56 │    28 ║      1 │     3 ║     3 │     3 ║
║     34 ║         0 ║  4563 ║    43 │    23 ║      1 │     3 ║     3 │     3 ║
║    119 ║       819 ║  3930 ║    54 │    28 ║      1 │     3 ║     3 │     3 ║
║    204 ║      1638 ║  4465 ║    78 │    43 ║      1 │     3 ║     3 │     3 ║
║    289 ║      2457 ║  3323 ║   121 │    74 ║      1 │     3 ║     3 │     3 ║
║    374 ║      3276 ║  3227 ║   189 │   129 ║      1 │     3 ║     3 │     3 ║
║    459 ║      4096 ║  3395 ║   315 │   232 ║      1 │     3 ║     3 │     3 ║
║        ║           ║  sum: ║  1567 │  1035 ║     11 │    33 ║    33 │    33 ║
║        ║           ║   +%: ║    +0 │    +0 ║ +14145 │ +3036 ║ +4648 │ +3036 ║
╚════════╩═══════════╩═══════╩═══════════════╩════════════════╩═══════════════╝
```

##### `mul(T,T): 128`

This test engages all multiplication algorithm for both `BigInt` and `BigInteger`, whereby the algorithm are designed to engage based on a threshold length of the underlying magnitude array.

The behavior of square multiplication is similar to the behavior of regular multiplication (where the input argument is not the same instance as the target object). It is, however, interesting to note that the Karatsuba algorithm runs faster for the "square" use-case, as the equality of `x` and `y` have a better change of allowing for in-place calculations.

```
[runtime performance]
mul(T,T): 128
  40921 in 134684ms
╔════════╦═══════════╦═══════╦════════════╦════════╦════════╗
║ length ║ precision ║ count ║ BigInteger ║ BigInt ║ int[]  ║
╠════════╬═══════════╬═══════╬════════════╬════════╬════════╣
║    884 ║     -8192 ║  2919 ║      99812 ║ 140967 ║ 135301 ║
║    714 ║     -6553 ║  3013 ║      75159 ║ 131859 ║ 125228 ║
║    544 ║     -4915 ║  2805 ║      42267 ║  92193 ║  91071 ║
║    374 ║     -3276 ║  3415 ║      24543 ║  48739 ║  48742 ║
║    204 ║     -1638 ║  5018 ║       5945 ║  11658 ║  11556 ║
║     34 ║         0 ║  5531 ║        386 ║    758 ║    766 ║
║    204 ║      1638 ║  4968 ║       5828 ║  11738 ║  11595 ║
║    374 ║      3276 ║  3415 ║      23798 ║  48926 ║  48581 ║
║    544 ║      4915 ║  2805 ║      43389 ║  92691 ║  91011 ║
║    714 ║      6553 ║  3013 ║      76385 ║ 130872 ║ 126977 ║
║    884 ║      8192 ║  2919 ║      97151 ║ 139767 ║ 135915 ║
║        ║           ║  sum: ║     494663 ║ 850168 ║ 826743 ║
║        ║           ║   +%: ║        +71 ║     +0 ║     +2 ║
╚════════╩═══════════╩═══════╩════════════╩════════╩════════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⣉⡩⠭⠝⠛⣛⠭⠝⠛⠉⠉⠉⠉⠉⠉⠉⠉⠛⠛⠭⣙⠛⠛⠭⠭⣉⣉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⠤⠤⠤⠒⠒⠉⠁⠀⠀⣠⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⢄⠀⠀⠀⠉⠑⠒⠒⠤⠤⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⠒⠊⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠢⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⣀⡠⠔⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠢⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⣀⡠⠔⠒⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠒⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠒⠢⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠊⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠑⠒⠒⠒⠒⠒⠒⠒⢺
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠳⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡤⠚⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⠦⣄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⠤⡲⠝⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠙⠶⣤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡗⠒⠒⠒⠒⠒⠒⠒⢒⣉⡡⠔⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠛⠵⣒⡤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⢼
⡏⠉⠉⠉⠉⠉⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

```

##### `umul(int[],long)`

This test validates the correctness of `[BigIntMultiplication.umul(long)][BigIntMultiplication]` against `[MPN.mul(...)][MPN]`.

```
[runtime performance]
umul(int[],long)
  289329 in 167ms
╔═════════╦═══════════╦═══════════════╦═══════════╦═══════════╗
║ length  ║ precision ║     count     ║    MPN    ║  BigInt   ║
╠════╤════╬═════╤═════╬═══════╤═══════╬═════╤═════╬═════╤═════╣
║ 36 │ 36 ║ -19 │ -19 ║ 37923 │ 37923 ║  13 │  14 ║   8 │   9 ║
║ 35 │ 35 ║ -15 │ -15 ║ 22700 │ 22700 ║  13 │  14 ║   8 │   9 ║
║ 35 │ 35 ║ -11 │ -11 ║ 30346 │ 30346 ║  14 │  14 ║   9 │   9 ║
║ 34 │ 34 ║  -7 │  -7 ║ 22723 │ 22723 ║  17 │  15 ║  11 │  10 ║
║ 34 │ 34 ║  -3 │  -3 ║ 30484 │ 30484 ║  16 │  15 ║  11 │  11 ║
║ 34 │ 34 ║   0 │   0 ║ 15284 │ 15284 ║  14 │  15 ║  10 │   9 ║
║ 34 │ 34 ║   3 │   3 ║ 30346 │ 30346 ║  17 │  14 ║  11 │   8 ║
║ 34 │ 34 ║   7 │   7 ║ 22700 │ 22700 ║  14 │  15 ║   8 │   8 ║
║ 35 │ 35 ║  11 │  11 ║ 30300 │ 30300 ║  13 │  14 ║   8 │   9 ║
║ 35 │ 35 ║  15 │  15 ║ 22700 │ 22700 ║  13 │  14 ║   8 │   9 ║
║ 36 │ 36 ║  19 │  19 ║ 22723 │ 22723 ║  13 │  14 ║   8 │  10 ║
║    │    ║     │     ║       │  sum: ║ 157 │ 158 ║ 100 │ 101 ║
║    │    ║     │     ║       │   +%: ║  +0 │  +0 ║ +57 │ +56 ║
╚════╧════╩═════╧═════╩═══════╧═══════╩═════╧═════╩═════╧═════╝
⡏⠉⠉⠉⠉⠉⠉⠉⠉⠉⠫⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢉⠭⠋⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠫⢍⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⢹
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠒⠤⠤⠤⠤⠤⠤⠤⠤⠤⠤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡠⠔⠒⠒⠒⠒⠒⠒⠒⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠤⠤⠤⠤⠤⠤⠤⢼
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⢄⠀⠀⠀⠀⠀⠀⠀⠀⡠⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠢⣀⠀⠀⢀⠤⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠑⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠒⠢⠤⠤⠤⠤⠤⢄⣀⣀⣀⣀⡠⠤⠤⠒⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠢⡀⠀⠀⠀⠀⠀⠀⠀⢀⠔⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠑⠤⣀⣀⣀⡠⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
⣇⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣸

[heap allocation]
umul(int[],long)
  7749 in 72ms
╔═════════╦═══════════╦════════════╦═══════════════╦═══════════════╗
║         ║           ║            ║      MPN      ║    BigInt     ║
║ length  ║ precision ║   count    ║     T │ int[] ║     T │ int[] ║
╠════╤════╬═════╤═════╬═════╤══════╬═══════╤═══════╬═══════╤═══════╣
║ 36 │ 36 ║ -19 │ -19 ║ 873 │  873 ║     0 │     0 ║     0 │     0 ║
║ 35 │ 35 ║ -15 │ -15 ║ 470 │  470 ║     0 │     0 ║     0 │     0 ║
║ 35 │ 35 ║ -11 │ -11 ║ 706 │  706 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║  -7 │  -7 ║ 493 │  493 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║  -3 │  -3 ║ 844 │  844 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║   0 │   0 ║ 464 │  464 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║   3 │   3 ║ 706 │  706 ║     0 │     0 ║     0 │     0 ║
║ 34 │ 34 ║   7 │   7 ║ 470 │  470 ║     0 │     0 ║     0 │     0 ║
║ 35 │ 35 ║  11 │  11 ║ 660 │  660 ║     0 │     0 ║     0 │     0 ║
║ 35 │ 35 ║  15 │  15 ║ 470 │  470 ║     0 │     0 ║     0 │     0 ║
║ 36 │ 36 ║  19 │  19 ║ 493 │  493 ║     0 │     0 ║     0 │     0 ║
║    │    ║     │     ║     │ sum: ║     0 │     0 ║     0 │     0 ║
║    │    ║     │     ║     │  +%: ║    +0 │    +0 ║    +0 │    +0 ║
╚════╧════╩═════╧═════╩═════╧══════╩═══════╧═══════╩═══════╧═══════╝
```

[BigIntAdditionTest]: src/test/java/org/libj/math/BigIntAdditionTest.java
[BigIntBinaryTest]: src/test/java/org/libj/math/BigIntBinaryTest.java
[BigIntBitwiseTest]: src/test/java/org/libj/math/BigIntBitwiseTest.java
[BigIntConstructorTest]: src/test/java/org/libj/math/BigIntConstructorTest.java
[BigIntDivisionTest]: src/test/java/org/libj/math/BigIntDivisionTest.java
[BigIntMultiplicationTest]: src/test/java/org/libj/math/BigIntMultiplicationTest.java
[BigIntPredicateTest]: src/test/java/org/libj/math/BigIntPredicateTest.java
[BigIntRemainderTest]: src/test/java/org/libj/math/BigIntRemainderTest.java

[BigIntegers]: src/main/java/org/libj/math/BigIntegers.java
[Numbers]: /../../../lang/src/main/java/org/libj/lang/Numbers.java

[bytebuddy]: https://bytebuddy.net/
[byteman]: https://byteman.jboss.org/