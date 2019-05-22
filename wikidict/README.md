# How to generate dictionary from Wikipedia NS0

## The Procedure

1.  Download dictionary files:

    *   <https://dumps.wikimedia.org/jawiki/> - YYYYMMDD/jawiki-YYYYMMDD-all-title-in-ns0.gz
    *   <https://dumps.wikimedia.org/enwiki/> - YYYYMMDD/enwiki-YYYYMMDD-all-title-in-ns0.gz

2.  Normalize JA wiki
    1.  Just normalize.

3.  Normalize EN wiki
    1.  truncate words.
    2.  normalize.

4.  convert both wiki data into a trietree file.
