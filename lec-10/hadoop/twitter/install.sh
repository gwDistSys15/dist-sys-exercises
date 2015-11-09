#!/bin/bash 
sudo yum -y install git gcc python-dev python-devel
sudo pip install -U numpy 
sudo pip install pyyaml nltk 
sudo pip install -e git://github.com/mdp-toolkit/mdp-toolkit#egg=MDP 
sudo python -m nltk.downloader -d /usr/share/nltk_data all 
