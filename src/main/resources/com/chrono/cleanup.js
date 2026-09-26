const fs = require('fs');
const items = JSON.parse(fs.readFileSync('./items-complete.json'));

let newItems = {};

Object.keys(items).map(id => {
    newItems[id] = {
        id: items[id].id,
        name: items[id].name,
        releaseDate: items[id].release_date,
        lastUpdated: items[id].last_updated,
    }
})

fs.writeFileSync('./items.json', JSON.stringify(newItems));