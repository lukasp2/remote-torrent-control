#!/usr/bin/env python3

# sudo apt-get install -y libnss3

from bs4 import BeautifulSoup
from pyppeteer import launch
import os
import json

from config import CONFIG

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

        browser = await launch(headless=True,
                               handleSIGINT=False,
                               handleSIGTERM=False,
                               handleSIGHUP=False,
                               args=['--no-sandbox'])
        page = await browser.newPage()
        url = 'https://thepiratebay.org/search.php?q=' + query
        await page.goto(url)
        html = await page.content()

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
                    'size' : item_soup.select("span.list-item.item-size")[0].text.replace('\u00a0', ' ').strip(),
                    'seed' : item_soup.select("span.list-item.item-seed")[0].text,
                    'leech' : item_soup.select("span.list-item.item-leech")[0].text.replace('\u00a0', ' ').strip(),
                    'user' : item_soup.select("span.list-item.item-user")[0].text,
                }
                
                torrents.append(torrent)

        await browser.close()

        return torrents
        
    def start_download(self, magnet):
        self.assert_VPN()
        command = 'transmission-remote -a ' + magnet
        output = os.popen(command).read()

        if self.command_check() == False:
            return CONFIG["FAIL_MSG"]

        return output # TODO
    
    def check_torrent_status(self):
        command = 'transmission-remote -l'
        output = os.popen(command).read()

        if self.command_check() == False:
            return CONFIG["FAIL_MSG"]

        return output # TODO: output { 'torrent name' : s, 'status' : i }

    def command_check(self):
        command = '$?'
        success_check = os.popen(command).read()
        return success_check == 0
    
