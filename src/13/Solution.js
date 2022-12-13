const fs = require('fs')

function comparePair(left, right) {
    for (let i = 0; i < left.length; i++) {
        if (i >= right.length) return -1
        if (typeof left[i] == 'number' && typeof right[i] == 'number') {
            if (left[i] < right[i]) return 1
            if (left[i] > right[i]) return -1
        } else if (typeof left[i] == 'number') {
            const result = comparePair([left[i]], right[i])
            if (result !== 0) return result
        } else if (typeof right[i] == 'number') {
            const result = comparePair(left[i], [right[i]])
            if (result !== 0) return result
        } else {
            const result = comparePair(left[i], right[i])
            if (result !== 0) return result
        }
    }
    if (left.length < right.length) return 1
    return 0
}

try {
    const data = fs.readFileSync('.input', 'utf8').split('\n')

    let i = 0
    let pair = []
    let pairs = []
    for (let d of data) {
        if (i < 2) {
            pair.push(JSON.parse(d.trim()))
        } else {
            pairs.push(pair)
            pair = []
        }
        i = (i + 1) % 3
    }
    pairs.push(pair)

    let result = 0

    pairs.map(it => comparePair(it[0], it[1])).forEach((x, i) => {
        if (x === 1) result += (i + 1)
    })
    console.log(result)

} catch (err) {
    console.error(err)
}

