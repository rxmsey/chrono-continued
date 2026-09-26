const fs = require('fs');

const spells = JSON.parse(fs.readFileSync('spells.json'));

let lastSpell = ["Fire Strike", 14286878, 35, 85];

spells.map(arr => {
    const s = arr[0];
    const enumName = s.replaceAll(' ', '_').replaceAll('-', '_').toUpperCase();
    console.log(`${enumName}("${s}", ${++lastSpell[1]}, ${++lastSpell[2]}, ${++lastSpell[3]}),`);
})