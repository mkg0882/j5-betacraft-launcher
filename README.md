# Betacraft Launcher

Betacraft launcher aims to provide easy access to old Minecraft versions and improve the overall game experience.

J5 Betacraft Launcher aims to run Betacraft Launcher on platforms that have no access to any Java newer than SE 5.

## FEATURES/CHANGES:
- Should support most features from the original project (see https://github.com/betacraftuk/betacraft-launcher/ for details).
  - Most notable feature removed is the Discord integration. Nothing else has been explicitly removed but some features may 
    have been inadvertently broken.
  - Raw HTTP requests are now handled by Apache Commons 'httpclient' with SSL/TLS requests routed through BouncyCastle. Java
    5's built-in methods do not seem capable of working nicely with BouncyCastle.
  - Gson was rolled back to ver. 2.4 for compatability with Java 5. Login and mod list loading both appear to still be working
    with this version but only login is fully tested.

## Supported platforms:
- This has (so far) been tested to work on Windows 10 and Mac OS X 10.5, both running their most recent releases of Java SE 5.

## Reporting bugs or requesting features
Report bugs in [issues](https://github.com/mkg0882/j5-betacraft-launcher/issues).

