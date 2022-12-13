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
    let packets = []
    for (let d of data) {
        if (i < 2) {
            packets.push(JSON.parse(d.trim()))
        }
        i = (i + 1) % 3
    }
    const x = [[6]]
    const y = [[2]]
    packets.push(x)
    packets.push(y)

    let result = 1

    const sorted = packets.sort((a, b) => comparePair(b, a))

    sorted.forEach((p, i) => {
        if (p === x || p === y) {
            result *= (i + 1)
        }
    })

    console.log(result)

} catch (err) {
    console.error(err)
}

