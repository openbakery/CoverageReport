# CoverageReport

CoverageReport convert the llvm profdata code coverage to text, HTML or XML


The following example generates a HTML coverage report of the [OBTableViewController](https://github.com/openbakery/OBTableViewController) project :
```
> ~/CoverageReport/bin/CoverageReport -b ./build/sym/Debug-iphonesimulator/Demo\ App.app/Demo\ App -p ./build/derivedData/Build/Intermediates/CodeCoverage/Demo\ App/Coverage.profdata  --type html --include "Core/Source/.*"
```

Here is the result: http://repo.openbakery.org/demo/coverage/index.html

## Usage:

```
> ~/Tools/CoverageReport/bin/CoverageReport
usage: CoverageReport [options]
 -b,--binary <binary>       Binary file (mandatory)
 -d,--debug                 Enable debug log
 -e,--exclude <exclude>     Files to exclude as regex pattern
 -h,--help                  Show usage information
 -i,--include <include>     Files to include as regex pattern
 -o,--output <output>       Output directory for the generated reports
 -p,--profdata <profdata>   Instrumentation-based profile file (mandatory)
 -r,--title <title>         Report title
 -t,--type <type>           Report type. Possible values: text, html or
                            xml
```

## Download

You can download a binary release here: http://openbakery.org/download/CoverageReport-0.9.2.zip