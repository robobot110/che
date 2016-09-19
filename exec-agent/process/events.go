package process

import "github.com/eclipse/che/exec-agent/op"

const (
	ProcessStartedEventType = "process_started"
	ProcessDiedEventType    = "process_died"
	StdoutEventType         = "process_stdout"
	StderrEventType         = "process_stderr"
)

type ProcessEventBody struct {
	op.EventBody
	Pid uint64 `json:"pid"`
}

type ProcessStatusEventBody struct {
	ProcessEventBody
	NativePid   int    `json:"nativePid"`
	Name        string `json:"name"`
	CommandLine string `json:"commandLine"`
}

type ProcessOutputEventBody struct {
	ProcessEventBody
	Text string `json:"text"`
}
