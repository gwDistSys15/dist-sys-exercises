# Hadoop Programming

## Setup S3 Access
On your koding.com VM run these commands:
```
sudo apt-get install s3cmd
s3cmd --configure
```
When prompted fill in these values:
```
Access Key: AKIAJGRETOWN7MCRECO   (ADD LETTER)
Secret Key: se/Phejnzx94fWILQeg+kBuo3HjLlze3hAjDzSA   (ADD LETTER) 
```
Use the default for all other options, except for the last where you must hit ``Y`` to save your changes.

You can now verify that S3 is working correctly by running this command to show the contents of our bucket:
```
s3cmd ls s3://gwdistsys-students
```

Next, create a text file containing the name of all students in your group and put it into your S3 bucket using the command line interface.  Log into the AWS web portal and use the S3 page to verify your file uploaded correctly.

## Python Test
Try to run the wordcount python program on your Koding.com VM:

```
cat romeo-short.txt | python wordcount.py | sort | python reduce.py
```

Upload the full ``romeo.txt`` file and ``wordcount.py`` to your S3 bucket.  Then use the website to submit a new Step in the EMR cluster. **DO NOT CREATE A NEW CLUSTER.** Instead, add a **Step** to the ``GW Dist Sys EMR`` cluster with these settings:

  - Mapper: ``s3://gwdistsys-students/YOURGROUP/wordcount.py``
  - Input: ``s3://gwdistsys-students/YOURGROUP/romeo.txt``
  - Output: ``s3://gwdistsys-students/YOURGROUP/UNIQUENAME``
  - Reducer: ``aggregate``

Watch the status of your job and look at the logs.  When the job completes, check the output. 

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

