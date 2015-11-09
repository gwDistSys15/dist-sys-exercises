# Hadoop Programming

## Airline Data
This data set includes information about all US flights, including information about delays.  The basic format of the data is:
```
"FL_DATE","CARRIER","FL_NUM","ORIGIN_CITY_NAME","ORIGIN_STATE_ABR","DEST_CITY_NAME","DEST_STATE_ABR","CRS_DEP_TIME","DEP_TIME","CRS_ARR_TIME","ARR_TIME","CANCELLED","AIR_TIME","DISTANCE",
```

Use this to answer at least one of these questions:

  - What is the percentage of late departures from every airport?
  - How many minutes total did passengers spend waiting for all late departures in 2014?
  - How many minutes of delay did each airline have?
  - Which date had the most minutes of delay in 2014?
    - Print one date per reducer
  - How many total flight hours did each airline have?


## Presidential Candidates
This data set includes tweets over the last twentyfour hours related to the presidential candidates Ben Carson, Hillary Clinton, Bernie Sanders, and Donalt Trump.  I've also provided a classifier that does sentiment analysis---given a string of text it indicates whether that string has a positive or negative attitude.  I've also provided a python script that will analyze all tweets to determine if they are positive or negative. 

First you should run an EMR Step that uses the existing program and examine its output.

  - Mapper: ``s3://gwdistsys-twitter/tweet.py``
  - Input: ``s3://gwdistsys-twitter/input``
  - Output: ``s3://gwdistsys-students/YOURGROUP/UNIQUENAME``
  - Reducer: ``aggregate``
  - Arguments: ``-cacheFile s3://gwdistsys-twitter/movclass.p#movclass.p`` 

When the program finishes, look at the output files from the reducers in your S3 folder.  **How many output files are there? Do they all have useful information in them? Why?**

Next, modify the ``tweet.py`` program so that it identifies the subject of the tweet before classifying it. The goal is to produce a count of positive tweets for each presidential candidate.  Every tweet will include the first and/or last name of a candidate. If a tweet contains more than one candidate's name you should ignore it.  The output of your program should be something like:

```
Carson-pos: XXX
Carson-neg: XXX
Clinton-pos: XXX
Clinton-neg: XXX
Sanders-pos: XXX
Sanders-neg: XXX
Trump-pos: XXXX
Trump-neg: XXXX
```

