#!/usr/bin/env python3

# sudo apt-get install -y libnss3

from bs4 import BeautifulSoup
from pyppeteer import launch
import os
import json

class TorrentHandler:
    def assert_VPN(self):
        command = 'nordvpn status'
        output = os.popen(command).read()
        if output.find('Status: Connected') == -1:
            command = 'nordvpn connect'
            os.popen(command).read()
            command = 'nordvpn status'
            output = os.popen(command).read()
            if output.find('Status: Connected') == -1:
                raise AssertionError("not connected to a VPN")

    async def search_torrents(self, query):
        self.assert_VPN()
        
        # launch the browser
        browser = await launch(headless=True, args=['--no-sandbox'])

        # open a new browser page
        page = await browser.newPage()
        
        url = 'https://thepiratebay.org/search.php?q=' + query
    
        # open our test file in the opened page
        await page.goto(url)
        html = await page.content()

        # process extracted content with BeautifulSoup
        soup = BeautifulSoup(html, 'lxml')

        results = soup.find_all('li', attrs={'id':'st'})
        
        torrents = []
    
        for result in results:
            if len(str(result)) > 0:
                item_soup = BeautifulSoup(str(result), 'lxml')

                torrent = {
                    'type' : item_soup.select('span.list-item.item-type')[0].text,
                    'name' : item_soup.select('span.list-item.item-name.item-title')[0].text,
                    'uploaded' : item_soup.select('span.list-item.item-uploaded')[0].text,
                    'size' : item_soup.select("span.list-item.item-size")[0].text,
                    'seed' : item_soup.select("span.list-item.item-seed")[0].text,
                    'leech' : item_soup.select("span.list-item.item-leech")[0].text,
                    'user' : item_soup.select("span.list-item.item-user")[0].text,
                }
                
                torrents.append(torrent)

        # close browser
        await browser.close()

        return torrents
        
    def start_download(self, magnet):
        self.assert_VPN()
        command = 'transmission-remote -a ' + magnet
        output = os.popen(command).read()
        return output # TODO: return success status
    
    def check_torrent_status(self):
        command = 'transmission-remote -l'
        output = os.popen(command).read()
        return output # TODO: return dict
    
