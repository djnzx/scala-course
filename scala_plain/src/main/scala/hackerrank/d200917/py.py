
from collections import Counter

def sherlockAndAnagrams(s):
    count = Counter(("".join(sorted(s[j:j+i])) for i in range(1,len(s)) for j in range(0,len(s)-i+1) ))
    return sum(sum(range(i)) for i in count.values())

for _ in range(int(input())):
    s = input()
    print(sherlockAndAnagrams(s))
