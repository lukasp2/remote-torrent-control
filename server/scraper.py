#!/usr/bin/env python3

# sudo apt-get install -y libnss3

import asyncio
from bs4 import BeautifulSoup
from pyppeteer import launch
import os
import json

async def search_torrents__(query):
    # launch the browser
    browser = await launch(headless=True, args=['--no-sandbox'])

    # open a new browser page
    page = await browser.newPage()

    query = 'batman'
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

def search_torrents(query):
    return asyncio.get_event_loop().run_until_complete(search_torrents(query))

