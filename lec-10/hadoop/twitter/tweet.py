#!/usr/bin/python

import cPickle as pickle
import nltk.classify.util
from nltk.classify import NaiveBayesClassifier
from nltk.tokenize import word_tokenize
import sys

sys.stderr.write("Started mapper.\n");

def word_feats(words):
    # Turn a string into a list of feature words
    return dict([(word, True) for word in words])

def main(argv):
    # Classifier trained with example from:
    # http://streamhacker.com/2010/05/10/text-classification-sentiment-analysis-naive-bayes-classifier/
    classifier = pickle.load(open("movclass.p", "r"))

    for line in sys.stdin:
        # split line into words and test with classifier
        tolk_posset = word_tokenize(line.rstrip())
        d = word_feats(tolk_posset)
        print "LongValueSum:" + classifier.classify(d) + "\t" + "1"

if __name__ == "__main__":
    main(sys.argv)


