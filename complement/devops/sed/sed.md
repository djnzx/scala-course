### finally, I'm supposed to learn sed...
[link](https://linuxhint.com/50_sed_command_examples/)

#### replace in the line - ONLY FIRST OCCURRENCE
```shell
echo "Bash Scripting Language" | sed 's/Bash/Perl/'
```

#### replace in the whole file - ONLY FIRST OCCURRENCE IN LINE
```shell
cat weekdays.txt | sed 's/Sunday/Sunday is a holiday/'
```

#### replace in the whole file - ALL OCCURRENCES
```shell
cat weekdays.txt | sed 's/Sunday/Sunday is a holiday/g'
```

#### extracting version  
```shell
cat version.sbt | sed 's/"\(.*\)".*/\1/'
```

#### 
```shell

```
