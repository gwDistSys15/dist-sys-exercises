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

Upload the full ``romeo.txt`` file,  ``wordcount.py`` and ``reduce.py`` to your S3 bucket.  Then use the website to submit a new Step in the EMR cluster. **DO NOT CREATE A NEW CLUSTER.** Instead, add a **Step** to the ``GW Dist Sys EMR`` cluster with these settings:

  - Mapper: ``s3://gwdistsys-students/YOURGROUP/wordcount.py``
  - Input: ``s3://gwdistsys-students/YOURGROUP/romeo.txt``
  - Output: ``s3://gwdistsys-students/YOURGROUP/UNIQUENAME``
  - Reducer: ``s3://gwdistsys-students/YOURGROUP/reduce.py``

**Warning:** the "Output" field must be a unique folder name--if you try to run two different jobs with the same output folder you will get an error!

Watch the status of your job and look at the logs.  When the job completes, check the output. 

Deliverables:
  - You do not need to turn anything in from this section.

## Airline Data
This data set includes information about all US flights, including information about delays.  The basic format of the data is:
```
"FL_DATE","CARRIER","FL_NUM","ORIGIN_CITY_NAME","ORIGIN_STATE_ABR","DEST_CITY_NAME","DEST_STATE_ABR","CRS_DEP_TIME","DEP_TIME","CRS_ARR_TIME","ARR_TIME","CANCELLED","AIR_TIME","DISTANCE",
```

Use this to answer at least **two** of these questions:

  - What is the percentage departures later than 5 minutes from every airport?
  - How many minutes total did passengers spend waiting for all late departures in 2014?
  - How many minutes of delay did each carrier have?
  - Which date had the most minutes of delay in 2014?
    - Print one date per reducer
  - How many total hours of airtime did each carrier have?
  - Some other interesting question you want to know about the data.

You must write map and reduce programs to solve two of these problems. You can base your programs on the ``wordcount.py`` and ``reducer.py`` programs in this repository.  Test your code locally on the small ``airline/airline-2014-small.txt`` file.  Then you should put your programs into S3 and run a hadoop task with the input data from the ``s3://gwdistsys-airline/input`` folder.

Deliverables:
  - Copies of your map and reduce programs used to solve two different problems listed above
  - A readme briefly describing your programs and giving some sample output (do not include the entire output files)

## Presidential Candidates
This data set includes tweets over the last twentyfour hours related to the presidential candidates Ben Carson, Hillary Clinton, Bernie Sanders, and Donald Trump.  I've provided a classifier that does sentiment analysis---given a string of text it indicates whether that string has a positive or negative attitude.  I've also provided a python mapper script that will analyze all tweets to determine if they are positive or negative. 

Note that in this case we will use one of the standard hadoop reducers, "aggregate", instead of writing our own. This is why in the ``tweet.py`` file when it is printing the keys and values it uses the ``LongValueSum:`` [prefix before the key name](https://github.com/gwDistSys15/dist-sys-exercises/blob/master/lec-10/hadoop/twitter/tweet.py#L24). This information is used by the ``aggregate`` reducer to know that it should sum together all values under a key and treat them as a long integer.  More info on other aggregators can be [found here](https://hadoop.apache.org/docs/r1.2.1/api/org/apache/hadoop/mapred/lib/aggregate/package-summary.html).

First you should run an EMR Step that uses the existing program and examine its output.

  - Mapper: ``s3://gwdistsys-twitter/tweet.py``
  - Input: ``s3://gwdistsys-twitter/input``
  - Output: ``s3://gwdistsys-students/YOURGROUP/UNIQUENAME``
  - Reducer: ``aggregate``
  - Arguments: ``-cacheFile s3://gwdistsys-twitter/movclass.p#movclass.p`` 


When the program finishes, look at the output files from the reducers in your S3 folder.  **How many output files are there? Do they all have useful information in them? Why?**  Answer these questions in your homework writeup.

Next, modify the ``tweet.py`` program so that it identifies the subject (i.e., named candidate) of the tweet before classifying it. The goal is to produce a count of positive tweets for each presidential candidate.  Every tweet will include the first and/or last name of a candidate. If a tweet contains more than one candidate's name you should ignore it.  The output of your program should be something like:

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

Deliverables:
  - Copies of your map and reduce programs
  - A description of your program in the readme file including the final output of your program listing the number of positive and negative tweets for each candidate
  - A screenshot of your EMR step with the completed status.
  
