package process

import (
	"github.com/eclipse/che/exec-agent/op"
	"time"
)

const (
	StartedEventType = "process_started"
	DiedEventType    = "process_died"
	StdoutEventType  = "process_stdout"
	StderrEventType  = "process_stderr"
)

type ProcessStatusEventBody struct {
	op.Timed
	Pid         uint64 `json:"pid"`
	NativePid   int    `json:"nativePid"`
	Name        string `json:"name"`
	CommandLine string `json:"commandLine"`
}

type ProcessOutputEventBody struct {
	op.Timed
	Pid  uint64 `json:"pid"`
	Text string `json:"text"`
}

func newStderrEvent(pid uint64, text string, when time.Time) *op.Event {
	return op.NewEvent(StderrEventType, &ProcessOutputEventBody{
		Timed: op.Timed{Time: when},
		Pid:  pid,
		Text: text,
	})
}

func newStdoutEvent(pid uint64, text string, when time.Time) *op.Event {
	return op.NewEvent(StdoutEventType, &ProcessOutputEventBody{
		Timed: op.Timed{Time: when},
		Pid:  pid,
		Text: text,
	})
}

func newStatusEvent(mp MachineProcess, status string) *op.Event {
	return op.NewEvent(status, &ProcessStatusEventBody{
		Timed: op.Timed{Time: time.Now()},
		Pid: mp.Pid,
		NativePid: mp.NativePid,
		Name: mp.Name,
		CommandLine: mp.CommandLine,
	})
}

func newStartedEvent(mp MachineProcess) *op.Event {
	return newStatusEvent(mp, StartedEventType)
}

func newDiedEvent(mp MachineProcess) *op.Event {
	return newStatusEvent(mp, DiedEventType)
}
