# CoverageReport

CoverageReport convert the llvm profdata code coverage to text, html or xml


The following example generates a HTML code coverage report of the [OBTableViewController](https://github.com/openbakery/OBTableViewController) project :
```
~/Tools/CoverageReport/bin/CoverageReport -b ./build/sym/Debug-iphonesimulator/Demo\ App.app/Demo\ App -p ./build/derivedData/Build/Intermediates/CodeCoverage/Demo\ App/Coverage.profdata  --type html --include "Core/Source/.*"
```

Here is how it looks like: http://openbakery.org/demo/coverage/index.html

## Download

You can download a binary release here: http://openbakery.org/download/CoverageReport-0.9.zip