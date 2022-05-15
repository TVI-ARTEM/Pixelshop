package ru.pixelshop.entities

import kotlin.math.max

class Project(val width: Int, val height: Int) {
    private val _frames = mutableListOf<Frame>()
    val frames: List<Frame>
        get() = _frames

    val framesCount: Int
        get() = _frames.size

    init {
        _frames.add(Frame(width = width, height = height, 1.0))
    }

    constructor(width: Int, height: Int, frame: Frame) : this(width = width, height = height) {
        _frames.clear()
        _frames.add(frame)
    }

    fun addFrame(frame: Frame): Frame {
        _frames.add(frame)
        return frame
    }

    fun addFrame(index: Int): Frame {
        require(index >= -1 && index < _frames.size) {
            "Index must be more than -1 or equals and less than ${_frames.size},  was: $index"
        }
        val newFrame = Frame(width = width, height = height, _speed = _frames[max(0, index)].speed)
        for (row in 0 until height) {
            for (column in 0 until width) {
                newFrame.update(row, column, _frames[max(0, index)].matrix[row][column])
            }
        }
        _frames.add(index = index + 1, newFrame)

        return newFrame
    }

    fun getFrame(index: Int): Frame {
        require(index >= 0 && index < _frames.size) {
            "Index must be non negative and less than ${_frames.size},  was: $index"
        }

        return _frames[index]
    }

    fun remove(index: Int): Frame {
        require(index >= 0 && index < _frames.size) {
            "Index must be non negative and less than ${_frames.size},  was: $index"
        }

        require(_frames.size > 1) {
            "Project must have at lest one frame"
        }

        return _frames.removeAt(index)
    }

    fun replace(oldIndex: Int, newIndex: Int) {
        require(oldIndex >= 0 && oldIndex < _frames.size && newIndex >= 0 && newIndex < _frames.size) {
            "Index must be non negative and less than ${_frames.size},  was: $oldIndex, $newIndex"
        }
        val frame = _frames.removeAt(oldIndex)
        _frames.add(newIndex, frame)
    }
}